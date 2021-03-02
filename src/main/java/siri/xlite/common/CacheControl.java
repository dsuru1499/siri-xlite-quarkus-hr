package siri.xlite.common;

import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import siri.xlite.model.SiriEntity;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CacheControl {
    public static final String PUBLIC = "public";
    public static final String MAX_AGE = "max-age=";
    public static final String S_MAX_AGE = "s-maxage=";
    public static final String PROXY_REVALIDATE = "proxy-revalidate";
    public static final String MUST_REVALIDATE = "must-revalidate";

    public static final String RECORDED_AT_TIME = "recordedAtTime";
    public static final Comparator<SiriEntity> COMPARATOR = Comparator.comparing(t -> t.recordedAtTime());

    public static Date getLastModified(RoutingContext context) {
        if (context != null) {
            String text = context.request().getHeader(HttpHeaders.IF_MODIFIED_SINCE);
            return StringUtils.isNotEmpty(text) ? DateTimeUtils.fromRFC1123(text) : null;
        }
        return null;
    }

    public static <T extends SiriEntity> Date getLastModified(T entity) {
        return (entity != null) ? entity.recordedAtTime() : null;
    }

    public static Date getLastModified(List<? extends SiriEntity> list) {
        Optional<? extends SiriEntity> result = list.stream().max(COMPARATOR);
        return result.map(CacheControl::getLastModified).orElse(null);
    }
}
