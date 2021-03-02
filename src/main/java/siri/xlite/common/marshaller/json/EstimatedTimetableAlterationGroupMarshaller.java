package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeField;

public class EstimatedTimetableAlterationGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String DATED_VEHICLE_JOURNEY_REF = "datedVehicleJourneyRef";
    public static final String EXTRA_JOURNEY = "extraJourney";
    public static final String CANCELLATION = "cancellation";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new EstimatedTimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set datedVehicleJourneyRef
        writeField(writer, DATED_VEHICLE_JOURNEY_REF, source.datedVehicleJourneyRef());

        // datedVehicleJourneyIndirectRef :string;
        // estimatedVehicleJourneyCode :string;

        // set extraJourney
        writeField(writer, EXTRA_JOURNEY, source.extraJourney());

        // set cancellation
        writeField(writer, CANCELLATION, source.cancellation());

    }

}