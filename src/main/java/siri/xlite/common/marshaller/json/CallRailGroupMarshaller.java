package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class CallRailGroupMarshaller implements Marshaller<Call> {

    public static final String PLATFORM_TRAVERSAL = "platformTraversal";

    @Getter
    private static final Marshaller<Call> instance = new CallRailGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // reversesAtStop :bool;

        // set platformTraversal
        writeField(writer, PLATFORM_TRAVERSAL, source.platformTraversal());

        // signalStatus :string;
    }

}