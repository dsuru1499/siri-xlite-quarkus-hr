package siri.xlite.service;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import gtfs.importer.GtfsImporter;
import gtfs.importer.Index;
import gtfs.model.*;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.UniEmitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.reactive.mutiny.Mutiny;
import siri.xlite.common.Color;
import siri.xlite.common.ZipUtils;
import siri.xlite.common.csv.*;
import uk.org.siri.siri.CallStatusEnumeration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Startup
@ApplicationScoped
public class BulkInitializer {
    public static final String TRUNCATE = "TRUNCATE TABLE %s CASCADE";
    public static final String COPY = "COPY %1$s %2$s FROM '%3$s' WITH CSV";
    public static final String DESTINATION_REF = "destinationRef";
    public static final String PLACE_NAME = "placeName";
    public static final String CSV = ".csv";
    public static final String VERSION_FILE = "version";
    private static final String ARCHIVE = "data.zip";
    private static final String DATA_DIR = "siri";
    private static final String UPDATE_SEQUENCE = "SELECT setval('%1$s_seq', max(id)) FROM %1$s;";
    private final MultiValuedMap<String, String> lineRefs = new HashSetValuedHashMap<>();
    private final MultiValuedMap<String, Map<String, String>> destinations = new HashSetValuedHashMap<>();
    @Inject
    Mutiny.SessionFactory factory;

    private GtfsImporter importer;

    @SuppressWarnings("unused")
    void onStart(@Observes StartupEvent event) {
        try {
            initialize();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void initialize() throws IOException {
        String temp = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(temp, DATA_DIR);
        Monitor monitor = MonitorFactory.start();
        log.info(Color.YELLOW + "[DSU] initialize model (~ 3mn)" + Color.NORMAL);

        if (checkArchive()) {
            this.importer = new GtfsImporter(path.toString());
            clear().call(this::fillVehicleJourney)
                    .call(this::fillAnnotatedLine)
                    .call(this::fillAnnotatedStopPoint)
                    .call(this::fillDestination)
                    .chain(this::copy)
                    .await().indefinitely();
            importer.dispose();
            Path version = Paths.get(temp, DATA_DIR, VERSION_FILE);
            File file = FileUtils.getFile(version.toString());
            FileUtils.touch(file);
        }
        log.info(Color.YELLOW + "[DSU] model initialized : " + monitor.stop() + Color.NORMAL);
    }


    private Uni<Void> truncate(Mutiny.Session session, String name) {
        String sql = String.format(TRUNCATE, name);
        return session.createNativeQuery(sql)
                .executeUpdate()
                .chain(t -> Uni.createFrom().voidItem());
    }

    private Uni<Void> clear() {
        final Monitor monitor = MonitorFactory.start("clear");

        Uni<Void> result = factory.withTransaction((session, transaction) -> truncate(session, "call")
                .call(() -> truncate(session, "vehiclejourney"))
                .call(() -> truncate(session, "destination"))
                .call(() -> truncate(session, "stoppoint"))
                .call(() -> truncate(session, "line")));

        return measure(result, monitor);
    }


    private Uni<Void> copy() {
        return factory.withTransaction((session, transaction) -> copy(session, LineBuilder.table(), LineBuilder.columns(), path(LineBuilder.table()))
                .call(() -> copy(session, StopPointBuilder.table(), StopPointBuilder.columns(), path(StopPointBuilder.table())))
                .call(() -> copy(session, DestinationBuilder.table(), DestinationBuilder.columns(), path(DestinationBuilder.table())))
                .call(() -> updateSequence(session, DestinationBuilder.table()))
                .call(() -> copy(session, VehicleJourneyBuilder.table(), VehicleJourneyBuilder.columns(), path(VehicleJourneyBuilder.table())))
                .call(() -> copy(session, CallBuilder.table(), CallBuilder.columns(), path(CallBuilder.table())))
                .call(() -> updateSequence(session, CallBuilder.table())))
                ;
    }

    private Uni<Void> fillAnnotatedLine() {
        final Monitor monitor = MonitorFactory.start(LineBuilder.table());

        Consumer<UniEmitter<? super Void>> create = (emitter) -> {
            try (BufferedWriter writer = Files.newBufferedWriter(path(LineBuilder.table()), StandardCharsets.UTF_8)) {
                LineBuilder.CsvBuilder builder = LineBuilder.builder();
                Date now = new Date();
                for (Route route : importer.getRouteById()) {
                    builder.recordedAtTime(now)
                            .lineRef(route.routeId())
                            .lineName(route.routeLongName())
                            .monitored(true);
                    writer.write(builder.build());
                }
                emitter.complete(null);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                emitter.fail(e);
            }
        };

        Uni<Void> result = Uni.createFrom().emitter(create)
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());

        return measure(result, monitor);
    }

    private Uni<Void> fillAnnotatedStopPoint() {
        final Monitor monitor = MonitorFactory.start(StopPointBuilder.table());

        Consumer<UniEmitter<? super Void>> create = (emitter) -> {
            try (BufferedWriter writer = Files.newBufferedWriter(path(StopPointBuilder.table()), StandardCharsets.UTF_8)) {
                StopPointBuilder.CsvBuilder builder = StopPointBuilder.builder();
                Date now = new Date();

                for (Stop stop : importer.getStopById()) {
                    builder.recordedAtTime(now)
                            .stopPointRef(stop.stopId())
                            .parent(stop.parentStation())
                            .stopName(stop.stopName())
                            .lineRefs(lineRefs.get(stop.stopId()))
                            .longitude(stop.stopLon().doubleValue())
                            .latitude(stop.stopLat().doubleValue())
                            .build();
                    writer.write(builder.build());
                }
                emitter.complete(null);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                emitter.fail(e);
            }
        };

        Uni<Void> result = Uni.createFrom().emitter(create)
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
        return measure(result, monitor);
    }

    private Uni<Void> fillVehicleJourney() {
        final Monitor monitor = MonitorFactory.start(VehicleJourneyBuilder.table());

        Consumer<UniEmitter<? super Void>> create = (emitter) -> {
            try (BufferedWriter vehicleJourneyWriter = Files.newBufferedWriter(path(VehicleJourneyBuilder.table()), StandardCharsets.UTF_8);
                 BufferedWriter callWriter = Files.newBufferedWriter(path(CallBuilder.table()), StandardCharsets.UTF_8)) {
                VehicleJourneyBuilder.CsvBuilder vehicleJourneyBuilder = VehicleJourneyBuilder.builder();
                CallBuilder.CsvBuilder callBuilder = CallBuilder.builder();

                Date now = new Date();
                int id = 1;

                for (Route route : importer.getRouteById()) {

                    Index<Agency> agencies = importer.getAgencyById();
                    Agency agency = agencies.getValue(route.agencyId());
                    Index<Trip> trips = importer.getTripByRoute();
                    for (Trip trip : trips.values(route.routeId())) {

                        vehicleJourneyBuilder
                                .delay(0L)
                                .recordedAtTime(now)
                                .bearing(0d)
                                .longitude(0d)
                                .latitude(0d)
                                .vehicleJourneyName(trip.tripId())
                                .operatorRef(agency.agencyId())
                                .directionName(trip.tripHeadSign())
                                .publishedLineName(route.routeLongName())
                                .routeRef(trip.tripId())
                                .vehicleModes(route.routeType().ordinal())
                                .journeyPatternName(trip.tripId())
                                .journeyPatternRef(trip.tripId())
                                .datedVehicleJourneyRef(trip.tripId())
                                .directionRef(trip.directionId().name())
                                .lineRef(route.routeId())
                                .monitored(true);

                        if (!filterByCalendar(trip)) {
                            continue;
                        }

                        Index<StopTime> stopTimes = importer.getStopTimeByTrip();
                        Iterator<StopTime> stopTimesIterator = stopTimes.values(trip.tripId()).iterator();
                        for (int i = 0; stopTimesIterator.hasNext(); i++) {
                            StopTime stopTime = stopTimesIterator.next();

                            Index<Stop> stops = importer.getStopById();
                            Stop stop = stops.getValue(stopTime.stopId());

                            lineRefs.put(stop.stopId(), route.routeId());

                            if (stop.locationType() == Stop.LocationType.Stop && stop.parentStation() != null
                                    && !stop.parentStation().isEmpty()) {
                                Stop station = stops.getValue(stop.parentStation());
                                if (station != null) {
                                    lineRefs.put(station.stopId(), route.routeId());
                                }
                            }
                            if (i == 0) {
                                vehicleJourneyBuilder.originRef(stop.stopId());
                                vehicleJourneyBuilder.originName(stop.stopName());
                                vehicleJourneyBuilder.originAimedDepartureTime(stopTime.departureTime().time());
                                vehicleJourneyBuilder.originExpectedDepartureTime(stopTime.departureTime().time());
                                vehicleJourneyBuilder.originDisplay(stop.stopName());
                            }

                            if (!stopTimesIterator.hasNext()) {
                                vehicleJourneyBuilder.destinationRef(stop.stopId());
                                vehicleJourneyBuilder.destinationName(stop.stopName());
                                vehicleJourneyBuilder.destinationAimedArrivalTime(stopTime.arrivalTime().time());
                                vehicleJourneyBuilder.destinationExpectedArrivalTime(stopTime.arrivalTime().time());
                                vehicleJourneyBuilder.destinationDisplay(stop.stopName());

                                Map<String, String> destination = new HashMap<>();
                                destination.put(DESTINATION_REF, stop.stopId());
                                destination.put(PLACE_NAME, stop.stopName());
                                destinations.put(route.routeId(), destination);
                            }

                            java.sql.Time aimedArrivalTime = stopTime.arrivalTime().time();
                            java.sql.Time aimedDepartureTime = stopTime.departureTime().time();
                            callBuilder.id(id++)
                                    .datedVehicleJourneyRef(trip.tripId())
                                    .index(i)
                                    .aimedArrivalTime(aimedArrivalTime)
                                    .expectedArrivalTime(aimedArrivalTime)
                                    .actualArrivalTime(aimedArrivalTime)
                                    .aimedDepartureTime(aimedDepartureTime)
                                    .expectedDepartureTime(aimedDepartureTime)
                                    .actualDepartureTime(aimedDepartureTime)
                                    .destinationDisplay(stopTime.stopHeadsign())
                                    .stopPointRef(stopTime.stopId())
                                    .order(stopTime.stopSequence() + 1)
                                    .departureStatus(CallStatusEnumeration.ON_TIME.ordinal())
                                    .arrivalStatus(CallStatusEnumeration.ON_TIME.ordinal());

                            callWriter.write(callBuilder.build());
                        }

                        vehicleJourneyWriter.write(vehicleJourneyBuilder.build());
                    }
                }
                emitter.complete(null);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                emitter.fail(e);
            }
        };

        Uni<Void> result = Uni.createFrom().emitter(create)
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
        return measure(result, monitor);
    }

    private Uni<Void> fillDestination() {
        final Monitor monitor = MonitorFactory.start(DestinationBuilder.table());

        Consumer<UniEmitter<? super Void>> create = (emitter) -> {
            try (BufferedWriter writer = Files.newBufferedWriter(path(DestinationBuilder.table()), StandardCharsets.UTF_8)) {
                DestinationBuilder.CsvBuilder builder = DestinationBuilder.builder();

                int id = 1;
                for (String lineRef : destinations.keySet()) {
                    for (Map<String, String> destination : destinations.get(lineRef)) {
                        String text = builder
                                .id(id++)
                                .destinationRef(destination.get(DESTINATION_REF))
                                .placeName(destination.get(PLACE_NAME))
                                .lineRef(lineRef)
                                .build();
                        writer.write(text);
                    }
                }
                emitter.complete(null);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                emitter.fail(e);
            }
        };

        Uni<Void> result = Uni.createFrom().emitter(create)
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
        return measure(result, monitor);
    }

    private boolean filterByCalendar(Trip trip) {
        boolean result = false;

        Calendar calendar = importer.getCalendarByService().getValue(trip.serviceId());
        CalendarDate calendarDate = importer.getCalendarDateByService().getValue(trip.serviceId());

        if (calendar != null) {

            LocalDate now = LocalDate.now();
            DayOfWeek day = now.getDayOfWeek();

            // LocalDate startDate = calendar.getStartDate().toLocalDate();
            // startDate = startDate.withYear(now.getYear());
            // LocalDate endDate = calendar.getEndDate().toLocalDate();
            // endDate = endDate.withYear(now.getYear());

            // if ((now.compareTo(startDate) >= 0) && (now.compareTo(endDate) <=
            // 0)) {
            switch (day) {
                case MONDAY:
                    result = calendar.monday();
                    break;
                case TUESDAY:
                    result = calendar.tuesday();
                    break;
                case WEDNESDAY:
                    result = calendar.wednesday();
                    break;
                case THURSDAY:
                    result = calendar.thursday();
                    break;
                case FRIDAY:
                    result = calendar.friday();
                    break;
                case SATURDAY:
                    result = calendar.saturday();
                    break;
                case SUNDAY:
                    result = calendar.sunday();
                    break;
                default:
                    break;
            }

            // }
            /*
             * if (calendarDate != null) { LocalDate exceptionDate = calendarDate.getDate().toLocalDate();
             * exceptionDate.withYear(now.getYear()); if (now.compareTo(exceptionDate) == 0) { switch
             * (calendarDate.getExceptionType()) { case Added: result = true; break; case Removed: result = false;
             * break; default: break; } } }
             */
        }

        return result;
    }

    private void extractArchive(Path path) throws IOException {
        Monitor monitor = MonitorFactory.start();
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File file = Paths.get(".", ARCHIVE).toFile();
        ZipUtils.unzipArchive(file, path.toFile());
        log.info(Color.YELLOW + "[DSU] extract archive : " + path + " " + monitor.stop() + Color.NORMAL);
    }

    private boolean checkArchive() throws IOException {
        String temp = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(temp, DATA_DIR);
        Path version = path.resolve(VERSION_FILE);
        if (Files.notExists(version)) {
            extractArchive(path);
            return true;
        } else {
            BasicFileAttributes attributes = Files.readAttributes(version, BasicFileAttributes.class);
            FileTime creation = attributes.creationTime();
            if (!DateUtils.isSameDay(new Date(), new Date(creation.toMillis()))) {
                Files.delete(version);
                return true;
            }
        }
        return false;
    }

    private Uni<Void> updateSequence(Mutiny.Session session, String table) {
        String sql = String.format(UPDATE_SEQUENCE, table);
        return session.createNativeQuery(sql)
                .executeUpdate()
                .chain(t -> Uni.createFrom().voidItem());
    }

    private Uni<Void> copy(Mutiny.Session session, String table, String columns, Path path) {
        final Monitor monitor = MonitorFactory.start(path.toString());
        String sql = String.format(COPY, table, columns, path);
        return session.createNativeQuery(sql)
                .executeUpdate()
                .onTermination().invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                .chain(t -> Uni.createFrom().voidItem());
    }

    private Path path(String tableName) {
        return Path.of(System.getProperty("java.io.tmpdir"), DATA_DIR, tableName + CSV);
    }

    private <T> Uni<T> measure(Uni<T> input, Monitor monitor) {
        return input.onSubscribe().invoke(monitor::start).onTermination()
                .invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL));
    }
}