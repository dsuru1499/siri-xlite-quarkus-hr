package siri.xlite.common;

import com.google.common.base.CaseFormat;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class ParametersFactory<T extends siri.xlite.common.Parameters> {

    private static final Map<Class<? extends siri.xlite.common.Parameters>, ParametersFactory<?>> _factories = new HashMap<>();

    public static <T extends siri.xlite.common.Parameters> void register(Class<T> clazz, ParametersFactory<T> factory) {
        _factories.put(clazz, factory);
    }

    @SuppressWarnings("unchecked")
    public static <T extends siri.xlite.common.Parameters> T create(Class<T> clazz, Configuration configuration, RoutingContext context)
            throws Exception {
        ParametersFactory<?> factory = _factories.get(clazz);
        if (factory == null) {
            try {
                Class.forName(getClassName(clazz));
                factory = _factories.get(clazz);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        siri.xlite.common.Parameters parameters = factory.create(configuration, context);
        parameters.validate();

        return (T) parameters;
    }

    private static <T extends siri.xlite.common.Parameters> String getClassName(Class<T> clazz) {
        String name = clazz.getSimpleName();
        String service = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                name.substring(0, name.lastIndexOf("Parameters")));
        return "siri.xlite.service." + service + "." + name + "Factory";
    }

    protected abstract T create(Configuration configuration, RoutingContext context) throws Exception;

}
