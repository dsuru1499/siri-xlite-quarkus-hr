package siri.xlite.service.stop_points_discovery;

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
import siri.xlite.model.StopPoint;
import siri.xlite.repositories.EtagsRepository;
import siri.xlite.repositories.StopPointRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class StopPointsDiscoveryService extends SiriService implements StopPointsDiscovery {
    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Inject
    Configuration configuration;
    @Inject
    StopPointRepository repository;
    @Inject
    EtagsRepository cache;

    @Route(path = APPLICATION + SEP + STOPPOINTS_DISCOVERY + SEP + COLON + X_TILE + SEP + COLON + Y_TILE,
            methods = HttpMethod.GET, type = Route.HandlerType.BLOCKING)
    @Route(path = APPLICATION + SEP + STOPPOINTS_DISCOVERY,
            methods = HttpMethod.GET, type = Route.HandlerType.BLOCKING)
    public void handle(RoutingContext context) {
        try {
            Monitor monitor = MonitorFactory.start(STOPPOINTS_DISCOVERY);
            // log(context.request());

            final StopPointsDiscoverySubscriber subscriber = new StopPointsDiscoverySubscriber();
            configure(subscriber, context)
                    .onItem().transformToMulti(t -> stream(t, context)).onCompletion()
                    .call(() -> onComplete(subscriber, context))
                    .onTermination().invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL))
                    .subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Uni<StopPointsDiscoveryParameters> configure(StopPointsDiscoverySubscriber subscriber, RoutingContext context) {
        try {
            StopPointsDiscoveryParameters result = ParametersFactory.create(StopPointsDiscoveryParameters.class,
                    configuration, context);
            subscriber.configure(result, context);
            return Uni.createFrom().item(result);
        } catch (Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Multi<StopPoint> stream(StopPointsDiscoveryParameters parameters,
                                    RoutingContext context) throws NotModifiedException {
        Date lastModified = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        cache.validate(uri, lastModified);
        if (parameters.getXtile() != null && parameters.getYtile() != null) {
            double[][] polygon = OSMUtils.location(parameters.getXtile(), parameters.getYtile());
            return repository.findByLocationRTree(polygon);
        }
        return repository.find();
    }

    private Uni<Void> onComplete(StopPointsDiscoverySubscriber subscriber, RoutingContext context) {
        cache.put(context.request().uri(), subscriber.getLastModified());
        return Uni.createFrom().voidItem();
    }
}
