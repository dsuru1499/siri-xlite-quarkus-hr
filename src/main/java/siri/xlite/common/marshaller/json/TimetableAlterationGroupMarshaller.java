package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeField;
import static siri.xlite.common.JsonUtils.writeObject;

public class TimetableAlterationGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String EXTRA_JOURNEY = "extraJourney";
    public static final String CANCELLATION = "cancellation";
    public static final String DATED_VEHICLE_JOURNEY_REF = "datedVehicleJourneytRef";
    public static final String FRAMED_VEHICLE_JOURNEY_REF = "framedVehicleJourneyRef";
    public static final String DATA_FRAME_REF = "dataFrameRef";
    public static final String ANY = "any";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new TimetableAlterationGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // FramedVehicleJourneyRef
        writeObject(writer, FRAMED_VEHICLE_JOURNEY_REF, source.datedVehicleJourneyRef(), datedVehicleJourneyRef -> {
            writeField(writer, DATED_VEHICLE_JOURNEY_REF, datedVehicleJourneyRef);
            writeField(writer, DATA_FRAME_REF, ANY);
        });

        // vehicleJourneyRef :string;

        // set extraJourney
        writeField(writer, EXTRA_JOURNEY, source.extraJourney());

        // set cancellation
        writeField(writer, CANCELLATION, source.cancellation());

    }

}
