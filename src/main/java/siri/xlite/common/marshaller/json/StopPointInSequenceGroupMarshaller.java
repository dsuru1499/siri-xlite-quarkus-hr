package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class StopPointInSequenceGroupMarshaller implements Marshaller<Call> {
    public static final String ORDER = "order";
    public static final String STOP_POINT_REF = "stopPointRef";
    public static final String STOP_POINT_NAME = "stopPointName";

    @Getter
    private static final Marshaller<Call> instance = new StopPointInSequenceGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set stopPointRef
        writeField(writer, STOP_POINT_REF, source.stopPoint().stopPointRef());

        // visitNumber :ushort;

        // set order
        writeField(writer, ORDER, source.order());

        // set stopPointName
        writeField(writer, STOP_POINT_NAME, source.stopPoint().stopName());

    }
}
