package siri.xlite.service.lines_discovery;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;
import siri.xlite.Configuration;
import siri.xlite.common.DefaultParameters;
import siri.xlite.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinesDiscoveryParameters extends DefaultParameters {

    @Override
    public void configure(Configuration configuration, RoutingContext context) throws SiriException {
        super.configure(configuration, context);
    }

    @Delegate
    public Configuration.ServiceConfiguration getConfigurationS() {
        return configuration.getLinesDiscovery();
    }
}
