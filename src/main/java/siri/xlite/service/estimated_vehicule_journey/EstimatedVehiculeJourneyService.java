package siri.xlite.service.estimated_vehicule_journey;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.Configuration;
import siri.xlite.common.*;
import siri.xlite.model.VehicleJourney;
import siri.xlite.repositories.EtagsRepository;
import siri.xlite.repositories.VehicleJourneyRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.ResourceBundle;

import static siri.xlite.common.Messages.LOAD_FROM_BACKEND;

@SuppressWarnings("unused")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class EstimatedVehiculeJourneyService extends SiriService implements EstimatedVehiculeJourney {
    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Inject
    Configuration configuration;
    @Inject
    VehicleJourneyRepository repository;
    @Inject
    EtagsRepository cache;

    @Route(path = APPLICATION + SEP + ESTIMATED_VEHICLE_JOURNEY, methods = HttpMethod.GET, type = Route.HandlerType.BLOCKING)
    public void handle(RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_VEHICLE_JOURNEY);
            // log(context.request());

            final EstimatedVehiculeJourneySubscriber subscriber = new EstimatedVehiculeJourneySubscriber();
            Uni<VehicleJourney> result = configure(subscriber, context)
                    .chain(t -> stream(t, context))
                    .call(() -> onComplete(subscriber, context))
                    .onTermination().invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL));
            result.subscribe().withSubscriber(new SubscriberWrapper<>(subscriber));

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
        log.info(messages.getString(LOAD_FROM_BACKEND), uri);
        return repository.find(parameters.getDatedVehicleJourneyRef());
    }

    private Uni<Void> onComplete(EstimatedVehiculeJourneySubscriber subscriber, RoutingContext context) {
        cache.put(context.request().uri(), subscriber.getLastModified());
        return Uni.createFrom().voidItem();
    }

}
