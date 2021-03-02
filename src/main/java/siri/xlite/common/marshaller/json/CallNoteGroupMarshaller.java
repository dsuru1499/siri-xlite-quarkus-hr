package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

public class CallNoteGroupMarshaller implements Marshaller<Call> {

    @Getter
    private static final Marshaller<Call> instance = new CallNoteGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // callNote :[string];
    }

}