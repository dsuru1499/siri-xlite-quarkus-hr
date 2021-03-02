package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

public class OperationalBlockGroupMarshaller implements Marshaller<VehicleJourney> {

    @Getter
    private static final Marshaller<VehicleJourney> instance = new OperationalBlockGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {
        // blockRef :string;
        // courseOfJourneyRef :string;
    }

}