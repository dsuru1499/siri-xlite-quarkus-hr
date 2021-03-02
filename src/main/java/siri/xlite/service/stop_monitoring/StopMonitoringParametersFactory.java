package siri.xlite.service.stop_monitoring;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri.xlite.Configuration;
import siri.xlite.common.ParametersFactory;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopMonitoringParametersFactory extends ParametersFactory<StopMonitoringParameters> {

    static {
        ParametersFactory.register(StopMonitoringParameters.class, new StopMonitoringParametersFactory());
    }

    @Override
    protected StopMonitoringParameters create(Configuration configuration, RoutingContext context) throws Exception {
        StopMonitoringParameters parameters = new StopMonitoringParameters();
        parameters.configure(configuration, context);
        return parameters;
    }
}
