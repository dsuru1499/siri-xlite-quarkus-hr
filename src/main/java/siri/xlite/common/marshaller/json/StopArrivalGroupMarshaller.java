package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri.xlite.common.JsonUtils.writeField;

public class StopArrivalGroupMarshaller implements Marshaller<Call> {

    public static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    public static final String ACTUAL_ARRIVAL_TIME = "actualArrivalTime";
    public static final String EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";
    public static final String ARRIVAL_PLATFORM_NAME = "arrivalPlatformName";
    public static final String ARRIVAL_STATUS = "arrivalStatus";

    @Getter
    private static final Marshaller<Call> instance = new StopArrivalGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set aimedArrivalTime
        writeField(writer, AIMED_ARRIVAL_TIME, source.aimedArrivalTime());

        // set actualArrivalTime
        writeField(writer, ACTUAL_ARRIVAL_TIME, source.actualArrivalTime());

        // set expectedArrivalTime
        writeField(writer, EXPECTED_ARRIVAL_TIME, source.expectedArrivalTime());

        // set latestExpectedArrivalTime

        // set arrivalStatus
        Integer arrivalStatus = source.arrivalStatus();
        if (arrivalStatus != null) {
            writeField(writer, ARRIVAL_STATUS, CallStatusEnumeration.values()[arrivalStatus]);
        }
        // arrivalProximityText :string;

        // set arrivalPlatformName
        writeField(writer, ARRIVAL_PLATFORM_NAME, source.arrivalPlatformName());

        // arrivalBoardingActivity :byte;

        // arrivalStopAssignment :StopAssignment;

        // set arrivalOperatorRefs

    }

}