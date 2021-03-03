package siri.xlite.service.estimated_timetable;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Multi;
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
import java.util.ResourceBundle;

import static siri.xlite.service.estimated_timetable.EstimatedTimetableParameters.LINE_REF;

@SuppressWarnings("unused")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class EstimatedTimetableService extends SiriService implements EstimatedTimetable {
    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Inject
    Configuration configuration;
    @Inject
    VehicleJourneyRepository repository;
    @Inject
    EtagsRepository cache;

    @Route(path = APPLICATION + SEP + ESTIMATED_TIMETABLE + SEP + COLON + LINE_REF,
            methods = HttpMethod.GET, type = Route.HandlerType.BLOCKING)
    public void handle(final RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(ESTIMATED_TIMETABLE);
            // log(context.request());

            final EstimatedTimetableSubscriber subscriber = new EstimatedTimetableSubscriber();
            Multi<VehicleJourney> result = configure(subscriber, context)
                    .onItem().transformToMulti(t -> stream(t, context))
                    .call(() -> onComplete(subscriber, context))
                    .onTermination().invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL));
            result.subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Uni<EstimatedTimetableParameters> configure(EstimatedTimetableSubscriber subscriber, RoutingContext context) {
        try {
            EstimatedTimetableParameters result = ParametersFactory.create(EstimatedTimetableParameters.class,
                    configuration, context);
            subscriber.configure(result, context);
            return Uni.createFrom().item(result);
        } catch (Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Multi<VehicleJourney> stream(EstimatedTimetableParameters parameters, RoutingContext context) throws NotModifiedException {
        String uri = context.request().uri();
        return repository.findByLineRef(parameters.getLineRef());
    }

    private Uni<Void> onComplete(EstimatedTimetableSubscriber subscriber, RoutingContext context) {
        long lifespan = configuration.getEstimatedTimetable().getSMaxAge();
        cache.put(context.request().uri(), subscriber.getLastModified(), lifespan);
        return Uni.createFrom().voidItem();
    }

}
