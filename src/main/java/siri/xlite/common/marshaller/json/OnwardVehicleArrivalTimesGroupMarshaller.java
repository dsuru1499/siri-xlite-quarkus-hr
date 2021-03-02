package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;

import static siri.xlite.common.JsonUtils.writeField;

public class OnwardVehicleArrivalTimesGroupMarshaller implements Marshaller<Call> {

    public static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    public static final String EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";

    @Getter
    private static final Marshaller<Call> instance = new OnwardVehicleArrivalTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set aimedArrivalTime
        writeField(writer, AIMED_ARRIVAL_TIME, source.aimedArrivalTime());

        // set expectedArrivalTime
        writeField(writer, EXPECTED_ARRIVAL_TIME, source.expectedArrivalTime());

        // expectedArrivalPredictionQuality :PredictionQuality;

    }

}
