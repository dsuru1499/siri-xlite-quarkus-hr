package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class StopProximityGroupMarshaller implements Marshaller<Call> {

    public static final String DISTANCE_FROM_STOP = "distanceFromStop";
    public static final String NUMBER_OF_STOPS_AWAY = "numberOfStopsAway";

    @Getter
    private static final Marshaller<Call> instance = new StopProximityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set distanceFromStop
        writeField(writer, DISTANCE_FROM_STOP, source.distanceFromStop());

        // set numberOfStopsAway
        writeField(writer, NUMBER_OF_STOPS_AWAY, source.numberOfStopsAway());

    }

}