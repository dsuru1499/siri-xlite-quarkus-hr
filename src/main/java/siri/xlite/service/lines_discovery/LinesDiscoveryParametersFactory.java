package siri.xlite.service.lines_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri.xlite.Configuration;
import siri.xlite.common.ParametersFactory;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinesDiscoveryParametersFactory extends ParametersFactory<LinesDiscoveryParameters> {

    static {
        ParametersFactory.register(LinesDiscoveryParameters.class, new LinesDiscoveryParametersFactory());
    }

    @Override
    protected LinesDiscoveryParameters create(Configuration configuration, RoutingContext context) throws Exception {
        LinesDiscoveryParameters parameters = new LinesDiscoveryParameters();
        parameters.configure(configuration, context);
        return parameters;
    }
}
