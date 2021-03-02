package siri.xlite.service.estimated_timetable;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri.xlite.Configuration;
import siri.xlite.common.ParametersFactory;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstimatedTimetableParametersFactory extends ParametersFactory<EstimatedTimetableParameters> {

    static {
        ParametersFactory.register(EstimatedTimetableParameters.class, new EstimatedTimetableParametersFactory());
    }

    @Override
    protected EstimatedTimetableParameters create(Configuration configuration, RoutingContext context)
            throws Exception {
        EstimatedTimetableParameters parameters = new EstimatedTimetableParameters();
        parameters.configure(configuration, context);
        return parameters;
    }
}
