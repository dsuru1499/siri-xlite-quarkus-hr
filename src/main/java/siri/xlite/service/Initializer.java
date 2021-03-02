package siri.xlite.service;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import gtfs.importer.GtfsImporter;
import gtfs.importer.Index;
import gtfs.model.*;
import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.Startup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.flywaydb.core.Flyway;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import siri.xlite.common.Color;
import siri.xlite.common.ZipUtils;
import siri.xlite.common.csv.*;
import uk.org.siri.siri.CallStatusEnumeration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Startup
@ApplicationScoped
public class Initializer {
    public static final String TRUNCATE = "TRUNCATE TABLE %s CASCADE";
    public static final String CURRVAL = "SELECT currval('%1$s_seq')";
    public static final String COPY = "COPY %1$s %2$s FROM STDIN WITH CSV";
    public static final String DESTINATION_REF = "destinationRef";
    public static final String PLACE_NAME = "placeName";
    private static final String ARCHIVE = "data.zip";
    private static final String OUTPUT_DIR = "siri";
    private static final String SETVAL = "SELECT setval('%1$s_seq', max(id)) FROM %1$s;";
    private static final String SEQUENCE = "ALTER SEQUENCE %1$s_seq RESTART WITH 1";
    private final MultiValuedMap<String, String> lineRefs = new HashSetValuedHashMap<>();
    private final MultiValuedMap<String, Map<String, String>> destinations = new HashSetValuedHashMap<>();

    @Inject
    AgroalDataSource dataSource;
    private GtfsImporter importer;

    @Inject
    Flyway flyway;

    @PostConstruct
    void onStartup() {
        checkMigration();
        String temp = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(temp, OUTPUT_DIR);
        if (Files.notExists(path)) {
            initialize();
        }
//        initialize();
    }

    public void checkMigration() {
//        flyway.clean();
        flyway.baseline();
        flyway.migrate();
        log.info(Color.GREEN + "[DSU] database version : " + flyway.info().current().getVersion().toString() + Color.NORMAL);
    }


    // @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Paris")
    public void initialize() {
        Monitor monitor = MonitorFactory.start();
        log.info(Color.YELLOW + "[DSU] initialize model (~ 2mn) " + Color.NORMAL);

        importer = inporter();
//        importer.getRouteById();
//        importer.getStopById();
//        importer.getAgencyById();
//        importer.getCalendarByService();
//        importer.getCalendarDateByService();
//        importer.getTripByRoute();
//        importer.getStopTimeByTrip();

        truncate();
        fillVehicleJourney();
        fillAnnotatedLine();
        fillAnnotatedStopPoint();
        fillDestination();
        copy();

        importer.dispose();
        log.info(Color.YELLOW + "[DSU] model initialized : " + monitor.stop() + Color.NORMAL);
    }

    private GtfsImporter inporter() {
        String temp = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(temp, OUTPUT_DIR);
        try {
            if (Files.notExists(path)) {
                extractArchive(path);
            }
            return new GtfsImporter(path.toString());
        } catch (Exception e) {
            try {
                FileUtils.deleteDirectory(path.toFile());
            } catch (IOException ignored) {
            }
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private void truncate() {
        try (Connection connection = dataSource.getConnection()
        ) {
            connection.setAutoCommit(false);
            truncate(connection, CallBuilder.table());
            truncate(connection, VehicleJourneyBuilder.table());
            truncate(connection, DestinationBuilder.table());
            truncate(connection, StopPointBuilder.table());
            truncate(connection, LineBuilder.table());
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void copy() {
        try {
            copy(LineBuilder.table(), LineBuilder.columns(), path(LineBuilder.table()));
            copy(StopPointBuilder.table(), StopPointBuilder.columns(), path(StopPointBuilder.table()));
            copy(DestinationBuilder.table(), DestinationBuilder.columns(), path(DestinationBuilder.table()));
            setval(DestinationBuilder.table());
            copy(VehicleJourneyBuilder.table(), VehicleJourneyBuilder.columns(), path(VehicleJourneyBuilder.table()));
            copy(CallBuilder.table(), CallBuilder.columns(), path(CallBuilder.table()));
            setval(CallBuilder.table());
        } catch (IOException | SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void fillAnnotatedLine() {
        final Monitor monitor = MonitorFactory.start(LineBuilder.table());

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
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private void fillAnnotatedStopPoint() {
        final Monitor monitor = MonitorFactory.start(StopPointBuilder.table());

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
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private void fillVehicleJourney() {
        final Monitor monitor = MonitorFactory.start(VehicleJourneyBuilder.table());

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
                            vehicleJourneyBuilder.originDisplay(stop.stopName());
                        }

                        if (!stopTimesIterator.hasNext()) {
                            vehicleJourneyBuilder.destinationRef(stop.stopId());
                            vehicleJourneyBuilder.destinationName(stop.stopName());
                            vehicleJourneyBuilder.destinationAimedArrivalTime(stopTime.arrivalTime().time());
                            vehicleJourneyBuilder.destinationDisplay(stop.stopName());

                            Map<String, String> destination = new HashMap<>();
                            destination.put(DESTINATION_REF, stop.stopId());
                            destination.put(PLACE_NAME, stop.stopName());
                            destinations.put(route.routeId(), destination);
                        }

                        java.sql.Time aimedArrivalTime = stopTime.arrivalTime().time();
                        java.sql.Time aimedDepartureTime = stopTime.departureTime().time();
                        callBuilder.id(id++)
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

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private void fillDestination() {

        final Monitor monitor = MonitorFactory.start(DestinationBuilder.table());

        try (BufferedWriter writer = Files.newBufferedWriter(path(DestinationBuilder.table()), StandardCharsets.UTF_8);
             Connection connection = dataSource.getConnection()) {
            DestinationBuilder.CsvBuilder builder = DestinationBuilder.builder();

            int id = 1;
            for (Map.Entry<String, Map<String, String>> entry : destinations.entries()) {
                String lineRef = entry.getKey();
                Map<String, String> destination = entry.getValue();
                String text = builder
                        .id(id++)
                        .destinationRef(destination.get(DESTINATION_REF))
                        .placeName(destination.get(PLACE_NAME))
                        .lineRef(lineRef)
                        .build();
                writer.write(text);
            }
        } catch (IOException | SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
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

        String temp = System.getProperty("java.io.tmpdir");
        Files.createDirectories(path);
        Path data = Paths.get(".", ARCHIVE);
        InputStream in = new BufferedInputStream(Files.newInputStream(data));

        File file = Paths.get(temp, OUTPUT_DIR, ARCHIVE).toFile();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        IOUtils.copy(in, out);
        out.close();
        in.close();
        log.info(Color.YELLOW + "[DSU] copy file : " + file + " " + Color.NORMAL);

        ZipUtils.unzipArchive(file, path.toFile());
        log.info(Color.YELLOW + "[DSU] extract archive : " + path + " " + monitor.stop() + Color.NORMAL);
    }

    private void truncate(Connection connection, String table) throws SQLException {
        Statement statement1 = connection.createStatement();
        String sql1 = String.format(TRUNCATE, table);
        System.out.println(sql1);
        statement1.executeUpdate(sql1);

//        Statement statement2 = connection.createStatement();
//        String sql2 = String.format(SEQUENCE, table);
//        System.out.println(sql2);
//        statement2.executeUpdate(sql2);

        connection.commit();
    }

    private void setval(String table) throws SQLException {
        final Monitor monitor = MonitorFactory.start(table);
        try (Connection connection = dataSource.getConnection()
        ) {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String sql = String.format(SETVAL, table);
            System.out.println(sql);
            statement.execute(sql);
            connection.commit();
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private void copy(String table, String columns, Path path) throws IOException, SQLException {
        final Monitor monitor = MonitorFactory.start(path.toString());
        try (BufferedReader reader = Files.newBufferedReader(path);
             Connection connection = dataSource.getConnection()
        ) {
            connection.setAutoCommit(false);
            CopyManager copyManager = connection.unwrap(PGConnection.class).getCopyAPI();
            String sql = String.format(COPY, table, columns);
            System.out.println(sql);
            copyManager.copyIn(sql, reader);
            connection.commit();
        } finally {
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private Path path(String tableName) {
        return Path.of(System.getProperty("java.io.tmpdir"), tableName + ".csv");
    }
}