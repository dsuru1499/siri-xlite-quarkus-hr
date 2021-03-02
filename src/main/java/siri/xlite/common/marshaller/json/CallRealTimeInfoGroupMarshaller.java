package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

public class CallRealTimeInfoGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new CallRealTimeInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // predictionInaccurate :bool;
        // occupancy :byte;

    }
}
