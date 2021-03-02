package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri.xlite.common.JsonUtils.writeField;

public class MonitoredStopArrivalStatusGroupMarshaller implements Marshaller<Call> {

    public static final String ARRIVAL_STATUS = "arrivalStatus";
    public static final String ARRIVAL_PLATFORM_NAME = "arrivalPlatformName";

    @Getter
    private static final Marshaller<Call> instance = new MonitoredStopArrivalStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

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
