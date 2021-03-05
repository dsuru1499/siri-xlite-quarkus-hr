package siri.xlite.service.lines_discovery;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import io.quarkus.vertx.web.Route;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.impl.Http2ServerRequestImpl;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.RoutingContextImpl;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;
import siri.xlite.Configuration;
import siri.xlite.common.*;
import siri.xlite.model.Line;
import siri.xlite.repositories.EtagsRepository;
import siri.xlite.repositories.LineRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class LinesDiscoveryService extends SiriService implements LinesDiscovery {
    private static final ResourceBundle messages = ResourceBundle
            .getBundle(Messages.class.getPackageName() + ".Messages");

    @Inject
    SessionFactory factory;

    @Inject
    LineRepository repository;
    @Inject
    Configuration configuration;
    @Inject
    EtagsRepository cache;

    @Route(path = APPLICATION + SEP + LINES_DISCOVERY,
            methods = HttpMethod.GET)
    public void handle(RoutingContext context) {
        try {
            final Monitor monitor = MonitorFactory.start(LINES_DISCOVERY);
            final LinesDiscoverySubscriber subscriber = new LinesDiscoverySubscriber();
            // log(context.request());
            configure(subscriber, context)
                    .onItem().transformToMulti(t -> stream(t, context))
                    .onCompletion().call(() -> onComplete(subscriber, context))
                    .onTermination().invoke(() -> log.info(Color.YELLOW + monitor.stop() + Color.NORMAL)).subscribe(subscriber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Uni<LinesDiscoveryParameters> configure(LinesDiscoverySubscriber subscriber, RoutingContext context) {
        try {
            LinesDiscoveryParameters result = ParametersFactory.create(LinesDiscoveryParameters.class, configuration,
                    context);
            subscriber.configure(result, context);
            return Uni.createFrom().item(result);
        } catch (Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Multi<Line> stream(LinesDiscoveryParameters parameters, RoutingContext context)
            throws NotModifiedException {
        Date lastModified = CacheControl.getLastModified(context);
        String uri = context.request().uri();
        cache.validate(uri, lastModified);
        return repository.find();
    }

    private Uni<Void> onComplete(LinesDiscoverySubscriber subscriber, RoutingContext context) {
        cache.put(context.request().uri(), subscriber.getLastModified());
        return Uni.createFrom().voidItem();
    }

}
