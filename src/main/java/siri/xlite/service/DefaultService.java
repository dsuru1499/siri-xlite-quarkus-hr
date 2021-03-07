package siri.xlite.service;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.Color;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.Locale;

import static siri.xlite.common.SiriService.APPLICATION;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class DefaultService {
    public static final String PUBLIC = "public";

    @Inject
    Vertx vertx;

    private Router router;

    @PostConstruct
    public void init(@Observes Router router) {
        this.router = router;
    }

    void onStart(@Observes StartupEvent event) {
        log.info(Color.GREEN + String.format("Application %s started", APPLICATION) + Color.NORMAL);
        log.info(Color.GREEN + String.format("Locale %s, Charset %s, Timezone %s",
                Locale.getDefault(), Charset.defaultCharset(), System.getProperty("user.timezone")) + Color.NORMAL);
        router.route().handler(FaviconHandler.create());
        router.route().handler(StaticHandler.create(PUBLIC));
    }

    void onStop(@Observes ShutdownEvent event) {
        log.info(Color.GREEN + String.format("Application %s stopped", APPLICATION) + Color.NORMAL);
    }

}
