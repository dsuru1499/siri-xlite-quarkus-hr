package siri.xlite.service.estimated_timetable;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;
import siri.xlite.Configuration;
import siri.xlite.common.DefaultParameters;
import siri.xlite.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstimatedTimetableParameters extends DefaultParameters {

    public static final String LINE_REF = "line_ref";

    private String lineRef;

    @Override
    public void configure(Configuration configuration, RoutingContext context) throws SiriException {
        super.configure(configuration, context);
        setLineRef(values.get(LINE_REF));
    }

    @Delegate
    public Configuration.ServiceConfiguration getConfigurationS() {
        return configuration.getEstimatedTimetable();
    }

}
