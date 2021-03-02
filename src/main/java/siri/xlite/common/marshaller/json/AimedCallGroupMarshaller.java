package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;
import uk.org.siri.siri.DepartureBoardingActivityEnumeration;

import static siri.xlite.common.JsonUtils.writeField;

public class AimedCallGroupMarshaller implements Marshaller<Call> {

    public static final String AIMED_ARRIVAL_TIME = "aimedArrivalTime";
    public static final String ARRIVAL_PLATFORM_NAME = "arrivalPlatformName";
    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";
    public static final String DEPARTURE_BOARDING_ACTIVITY = "departureBoardingActivity";
    public static final String AIMED_HEADWAY_INTERVAL = "aimedHeadwayInterval";

    @Getter
    private static final Marshaller<Call> instance = new AimedCallGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set aimedArrivalTime
        writeField(writer, AIMED_ARRIVAL_TIME, source.aimedArrivalTime());

        // set arrivalPlatformName
        writeField(writer, ARRIVAL_PLATFORM_NAME, source.arrivalPlatformName());

        // arrivalBoardingActivity :byte;

        // set arrivalOperatorRefs

        // set aimedDepartureTime
        writeField(writer, AIMED_DEPARTURE_TIME, source.aimedDepartureTime());

        // set departurePlatformName
        writeField(writer, DEPARTURE_PLATFORM_NAME, source.departurePlatformName());

        // set departureBoardingActivity
        Integer departureBoardingActivity = source.departureBoardingActivity();
        if (departureBoardingActivity != null) {
            writeField(writer, DEPARTURE_BOARDING_ACTIVITY,
                    DepartureBoardingActivityEnumeration.values()[departureBoardingActivity]);
        }
        // set departureOperatorRefs

        // aimedLatestPassengerAccessTime :long;

        // set aimedHeadwayInterval
        writeField(writer, AIMED_HEADWAY_INTERVAL, source.aimedHeadwayInterval());

    }
}
