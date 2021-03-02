package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeArray;
import static siri.xlite.common.JsonUtils.writeField;

public class JourneyInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String VEHICLE_JOURNEY_NAME = "vehicleJourneyName";
    public static final String JOURNEY_NOTE = "journeyNote";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set vehicleJourneyName
        writeField(writer, VEHICLE_JOURNEY_NAME, source.vehicleJourneyName());

        // set journeyNote

        writeArray(writer, JOURNEY_NOTE, source.journeyNotes());

        // publicContact :SimpleContact;
        // operationsContact:SimpleContact;
    }

}
