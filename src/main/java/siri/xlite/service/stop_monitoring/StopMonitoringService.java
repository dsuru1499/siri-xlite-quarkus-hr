package siri.xlite.service.stop_monitoring;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;
import siri.xlite.Configuration;
import siri.xlite.common.*;
import siri.xlite.model.StopPoint;
import siri.xlite.model.Tuple;
import siri.xlite.model.VehicleJourney;
import siri.xlite.repositories.EtagsRepository;
import siri.xlite.repositories.StopPointRepository;
import siri.xlite.repositories.VehicleJourneyRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Time;
import java.util.TreeMap;
import java.util.function.Function;

import static siri.xlite.service.stop_monitoring.StopMonitoringParameters.MONITORING_REF;

@SuppressWarnings("unused")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class StopMonitoringService extends SiriService implements StopMonitoring {

    @Inject
    protected SessionFactory factory;

    @Inject
    Configuration configuration;
    @Inject
    VehicleJourneyRepository vehicleJourneyRepository;
    @Inject
    StopPointRepository stopPointRepository;
    @Inject
    EtagsRepository cache;

    @Route(path = APPLICATION + SEP + STOP_MONITORING + SEP + COLON + MONITORING_REF,
            methods = HttpMethod.GET)
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOP_MONITORING);

            final StopMonitoringSubscriber subscriber = new StopMonitoringSubscriber();
            configure(subscriber, context)
                    .onItem().transformToMulti(t -> stream(t, context))
                    .onCompletion().call(() -> onComplete(subscriber, context))
                    .onTermination().invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Uni<StopMonitoringParameters> configure(StopMonitoringSubscriber subscriber, RoutingContext context) {
        try {
            StopMonitoringParameters result = ParametersFactory.create(StopMonitoringParameters.class, configuration,
                    context);
            subscriber.configure(result, context);
            return Uni.createFrom().item(result);
        } catch (Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Multi<Tuple<VehicleJourney, Integer>> stream(StopMonitoringParameters parameters,
                                                         RoutingContext context) throws NotModifiedException {
        String uri = context.request().uri();
        Time now = DateTimeUtils.now();

        Function<Tuple<VehicleJourney, Integer>, Time> extractor = t -> t.left().calls().get(t.right()).expectedDepartureTime();

//        Supplier<SortedMap<Time, Tuple<VehicleJourney, Integer>>> supplier = () -> {
//            Comparator<? super Tuple<VehicleJourney, Integer>> comparator = Comparator.comparing(extractor);
//            return new TreeMap(comparator);
//        };


        return stopPointRepository.findByMonitoringRef(parameters.getMonitoringRef())
                .map(StopPoint::stopPointRef).collect().asList().onItem()
                .transformToMulti(stopPointRefs -> vehicleJourneyRepository.findByStopPointRefs(stopPointRefs))
                .concatMap(t -> Multi.createFrom().range(0, t.calls().size())
                        .map(i -> Tuple.of(t, i)))
                .filter(t -> {
                    Time expectedDepartureTime = extractor.apply(t);
                    return expectedDepartureTime.after(now);
                })
                .collect().in(TreeMap<Time, Tuple<VehicleJourney,Integer>>::new, (map, t) -> map.put(extractor.apply(t), t))
                .onItem().transformToMulti(t -> Multi.createFrom().iterable(t.values()));


//                .onItem().transformToMulti(list -> {
//                    t.left().calls().get(t.right()).expectedDepartureTime()
//                    list.stream().filter()
//                    list.sort(Comparator.comparing(t ->));
//                    return Multi.createFrom().iterable(list);
//                });
        // ! PB mapping native sql
        // return vehicleJourneyRepository.findByMonitoringRef(session, parameters.getMonitoringRef())
        // .concatMap(t -> {
        // return Multi.createFrom().range(0, t.calls().size())
        // .map(i -> Tuple.of(t, i));
        // })
        // .collect().asList().onItem().transformToMulti(list -> {
        // list.sort(Comparator.comparing(t -> t.left().calls().get(t.right()).expectedDepartureTime()));
        // return Multi.createFrom().iterable(list);
        // });
    }

    private Uni<Void> onComplete(StopMonitoringSubscriber subscriber, RoutingContext context) {
        long lifespan = configuration.getStopMonitoring().getSMaxage();
        cache.put(context.request().uri(), subscriber.getLastModified(), lifespan);
        return Uni.createFrom().voidItem();
    }
}
