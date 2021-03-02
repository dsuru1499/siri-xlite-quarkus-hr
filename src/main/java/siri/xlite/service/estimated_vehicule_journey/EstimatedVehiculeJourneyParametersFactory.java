package siri.xlite.service.estimated_vehicule_journey;

import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import siri.xlite.Configuration;
import siri.xlite.common.ParametersFactory;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstimatedVehiculeJourneyParametersFactory extends ParametersFactory<EstimatedVehiculeJourneyParameters> {

    static {
        ParametersFactory.register(EstimatedVehiculeJourneyParameters.class,
                new EstimatedVehiculeJourneyParametersFactory());
    }

    @Override
    protected EstimatedVehiculeJourneyParameters create(Configuration configuration, RoutingContext context)
            throws Exception {
        EstimatedVehiculeJourneyParameters parameters = new EstimatedVehiculeJourneyParameters();
        parameters.configure(configuration, context);
        return parameters;
    }
}
