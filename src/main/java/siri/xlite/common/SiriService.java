package siri.xlite.common;

import com.jamonapi.Monitor;
import io.smallrye.mutiny.Uni;
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

}
