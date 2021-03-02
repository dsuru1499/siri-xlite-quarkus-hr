package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class CallPropertyGroupMarshaller implements Marshaller<Call> {

    public static final String ORIGIN_DISPLAY = "originDisplay";
    public static final String DESTINATION_DISPLAY = "destinationDisplay";

    @Getter
    private static final Marshaller<Call> instance = new CallPropertyGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // timingPoint :bool;
        // boardingStretch :bool;
        // requestStop :bool;

        // set originDisplay
        writeField(writer, ORIGIN_DISPLAY, source.originDisplay());

        // set destinationDisplay
        writeField(writer, DESTINATION_DISPLAY, source.destinationDisplay());

    }

}
