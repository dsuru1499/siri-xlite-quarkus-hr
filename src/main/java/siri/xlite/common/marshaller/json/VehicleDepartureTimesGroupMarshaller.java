package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class VehicleDepartureTimesGroupMarshaller implements Marshaller<Call> {

    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String ACTUAL_DEPARTURE_TIME = "actualDepartureTime";
    public static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";

    @Getter
    private static final Marshaller<Call> instance = new VehicleDepartureTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set aimedDepartureTime
        writeField(writer, AIMED_DEPARTURE_TIME, source.aimedDepartureTime());

        // set actualDepartureTime
        writeField(writer, ACTUAL_DEPARTURE_TIME, source.actualDepartureTime());

        // set expectedDepartureTime
        writeField(writer, EXPECTED_DEPARTURE_TIME, source.expectedDepartureTime());

    }
}
