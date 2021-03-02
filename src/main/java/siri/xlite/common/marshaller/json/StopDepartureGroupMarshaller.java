package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;
import uk.org.siri.siri.CallStatusEnumeration;

import static siri.xlite.common.JsonUtils.writeField;

public class StopDepartureGroupMarshaller implements Marshaller<Call> {

    public static final String AIMED_DEPARTURE_TIME = "aimedDepartureTime";
    public static final String ACTUAL_DEPARTURE_TIME = "actualDepartureTime";
    public static final String EXPECTED_DEPARTURE_TIME = "expectedDepartureTime";
    public static final String DEPARTURE_STATUS = "departureStatus";
    public static final String DEPARTURE_PLATFORM_NAME = "departurePlatformName";

    @Getter
    private static final Marshaller<Call> instance = new StopDepartureGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Call source) {

        // set aimedDepartureTime
        writeField(writer, AIMED_DEPARTURE_TIME, source.aimedDepartureTime());

        // set actualDepartureTime
        writeField(writer, ACTUAL_DEPARTURE_TIME, source.actualDepartureTime());

        // set expectedDepartureTime
        writeField(writer, EXPECTED_DEPARTURE_TIME, source.expectedDepartureTime());

        // provisionalExpectedDepartureTime :long;

        // earliestExpectedDepartureTime :long;

        // expectedDeparturePredictionQuality :PredictionQuality;

        // aimedLatestPassengerAccessTime :long;

        // expectedLatestPassengerAccessTime :long;

        // set departureStatus
        Integer departureStatus = source.departureStatus();
        if (departureStatus != null) {
            writeField(writer, DEPARTURE_STATUS, CallStatusEnumeration.values()[departureStatus]);
        }

        // departureProximityText :string;

        // set departurePlatformName
        writeField(writer, DEPARTURE_PLATFORM_NAME, source.departurePlatformName());

        // departureBoardingActivity :byte;

        // departureStopAssignment :StopAssignment;

        // departureOperatorRefs

    }
}