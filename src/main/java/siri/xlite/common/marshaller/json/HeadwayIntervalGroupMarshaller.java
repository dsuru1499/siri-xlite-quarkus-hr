package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class HeadwayIntervalGroupMarshaller implements Marshaller<Call> {

    public static final String AIMED_HEADWAY_INTERVAL = "aimedHeadwayInterval";
    public static final String EXPECTED_HEADWAY_INTERVAL = "expectedHeadwayInterval";

    @Getter
    private static final Marshaller<Call> instance = new HeadwayIntervalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set aimedHeadwayInterval
        writeField(writer, AIMED_HEADWAY_INTERVAL, source.aimedHeadwayInterval());

        // set expectedHeadwayInterval
        writeField(writer, EXPECTED_HEADWAY_INTERVAL, source.expectedHeadwayInterval());

    }
}