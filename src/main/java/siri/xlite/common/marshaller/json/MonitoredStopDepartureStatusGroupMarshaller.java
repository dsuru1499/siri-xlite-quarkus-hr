package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri.xlite.common.JsonUtils.writeField;

public class MonitoredStopDepartureStatusGroupMarshaller implements Marshaller<Call> {

    public static final String DEPARTURE_STATUS = "departureStatus";
    public static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";
    public static final String DEPARTURE_BOARDING_ACTIVITY = "departureBoardingActivity";

    @Getter
    private static final Marshaller<Call> instance = new MonitoredStopDepartureStatusGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set departureStatus
        Integer departureStatus = source.departureStatus();
        if (departureStatus != null) {
            writeField(writer, DEPARTURE_STATUS, CallStatusEnumeration.values()[departureStatus]);
        }

        // departureProximityText :string;

        // set departurePlatformName
        writeField(writer, DEPARTURE_PLATFORM_NAME, source.departurePlatformName());

        // set departureBoardingActivity
        writeField(writer, DEPARTURE_BOARDING_ACTIVITY, source.departureBoardingActivity());

        // departureStopAssignment :StopAssignment;

        // set departureOperatorRefs

    }
}