package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class CallRealtimeGroupMarshaller implements Marshaller<Call> {

    public static final String VEHICLE_AT_STOP = "vehicleAtStop";

    @Getter
    private static final Marshaller<Call> instance = new CallRealtimeGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set vehicleAtStop
        writeField(writer, VEHICLE_AT_STOP, source.vehicleAtStop());

        // vehicleLocationAtStop : Location;
    }

}