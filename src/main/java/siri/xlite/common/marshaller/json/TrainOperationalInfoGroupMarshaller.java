package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.*;

public class TrainOperationalInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String TRAIN_NUMBERS = "trainNumbers";
    public static final String JOURNEY_PARTS = "journeyParts";
    public static final String JOURNEY_PART_REF = "journeyPartRef";
    public static final String TRAIN_NUMBER_REF = "trainNumberRef";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new TrainOperationalInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // trainBlockPart : [TrainBlockPart];
        // blockRef :string;
        // courseOfJourneyRef :string;
        // DocumentRef :string;
        // vehicleRef :string;
        // additionalDocumentRef :[string];
        // driverRef :string;
        // driverName :string;

        // set trainNumbers

        writeArray(writer, TRAIN_NUMBERS, source.trainNumbers(),
                t -> writeObject(writer, t, trainNumber -> writeField(writer, TRAIN_NUMBER_REF, trainNumber)));

        // set journeyParts
        writeArray(writer, JOURNEY_PARTS, source.journeyParts(), t -> writeObject(writer, t, o -> {
            writeField(writer, JOURNEY_PART_REF, o.journeyPartRef());
            writeField(writer, TRAIN_NUMBER_REF, o.trainNumberRef());
        }));

    }
}