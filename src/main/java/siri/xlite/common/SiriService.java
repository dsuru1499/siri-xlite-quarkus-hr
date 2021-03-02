package siri.xlite.common;

import com.jamonapi.Monitor;
import io.smallrye.mutiny.Uni;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SiriService {

    public static final String APPLICATION = "/siri-xlite";
    public static final String SEP = "/";
    public static final String COLON = ":";
    public static final String HASH = "#";
    public static final String PUBLIC = "public";
    public static final String X_TILE = "x";
    public static final String Y_TILE = "y";

    protected <T> Uni<T> measure(Uni<T> input, Monitor monitor) {
        return input.onSubscribe().invoke(monitor::start).onTermination()
                .invoke(() -> log.info(siri.xlite.common.Color.YELLOW + monitor.stop() + siri.xlite.common.Color.NORMAL));
    }

    protected void log(HttpServerRequest request) {
        log.info(siri.xlite.common.Color.GREEN + "[DSU] GET " + request.absoluteURI() + siri.xlite.common.Color.NORMAL);
        log.info(siri.xlite.common.Color.GREEN + "[DSU] GET " + request.host() + siri.xlite.common.Color.NORMAL);
        MultiMap headers = request.headers();
        for (String key : headers.names()) {
            String value = String.join(",", headers.getAll(key));
            log.info(siri.xlite.common.Color.GREEN + key + "=" + value + siri.xlite.common.Color.NORMAL);
        }
    }

}
