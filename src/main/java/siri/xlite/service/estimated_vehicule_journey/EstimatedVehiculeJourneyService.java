package siri.xlite.service.estimated_vehicule_journey;

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
import siri.xlite.model.VehicleJourney;
import siri.xlite.repositories.EtagsRepository;
import siri.xlite.repositories.VehicleJourneyRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;

import static siri.xlite.service.estimated_vehicule_journey.EstimatedVehiculeJourneyParameters.DATED_VEHICLE_JOURNEY_REF;

@SuppressWarnings("unused")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class EstimatedVehiculeJourneyService extends SiriService implements EstimatedVehiculeJourney {

    @Inject
    protected SessionFactory factory;

    @Inject
    Configuration configuration;
    @Inject
    VehicleJourneyRepository repository;
    @Inject
    EtagsRepository cache;

    @Route(path = APPLICATION + SEP + ESTIMATED_VEHICLE_JOURNEY + SEP + COLON + DATED_VEHICLE_JOURNEY_REF,
            methods = HttpMethod.GET)
    public void handle(RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_VEHICLE_JOURNEY);
//            log(context.request());

            final EstimatedVehiculeJourneySubscriber subscriber = new EstimatedVehiculeJourneySubscriber();
            configure(subscriber, context)
                    .chain(t -> stream(t, context))
                    .onItem().transformToMulti(t -> Multi.createFrom().items(t))
                    .onCompletion().call(() -> onComplete(subscriber, context))
                    .onTermination().invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Uni<EstimatedVehiculeJourneyParameters> configure(EstimatedVehiculeJourneySubscriber subscriber,
                                                              RoutingContext context) {
        try {
            EstimatedVehiculeJourneyParameters result = ParametersFactory
                    .create(EstimatedVehiculeJourneyParameters.class, configuration, context);
            subscriber.configure(result, context);
            return Uni.createFrom().item(result);
        } catch (Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Uni<VehicleJourney> stream(EstimatedVehiculeJourneyParameters parameters,
                                       RoutingContext context) throws NotModifiedException {
        Date lastModified = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        cache.validate(uri, lastModified);
        return repository.find(parameters.getDatedVehicleJourneyRef());
    }

    private Uni<Void> onComplete(EstimatedVehiculeJourneySubscriber subscriber, RoutingContext context) {
        cache.put(context.request().uri(), subscriber.getLastModified());
        return Uni.createFrom().voidItem();
    }

}
