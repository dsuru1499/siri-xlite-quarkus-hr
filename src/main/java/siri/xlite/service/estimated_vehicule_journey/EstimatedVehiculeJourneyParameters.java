package siri.xlite.service.estimated_vehicule_journey;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;
import siri.xlite.Configuration;
import siri.xlite.common.DefaultParameters;
import siri.xlite.common.SiriException;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstimatedVehiculeJourneyParameters extends DefaultParameters {

    public static final String DATED_VEHICLE_JOURNEY_REF = "dated_vehicle_journey_ref";

    private String datedVehicleJourneyRef;

    @Override
    public void configure(Configuration configuration, RoutingContext context) throws SiriException {
        super.configure(configuration, context);
        setDatedVehicleJourneyRef(values.get(DATED_VEHICLE_JOURNEY_REF));
    }

    @Delegate
    public Configuration.ServiceConfiguration getConfigurationS() {
        return configuration.getEstimatedVehicleJourney();
    }
}
