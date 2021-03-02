package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

import static siri.xlite.common.JsonUtils.writeField;

public class JourneyEndTimesGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String ORIGIN_AIMED_DEPARTURE_TIME = "originAimedDepartureTime";
    public static final String HEADWAY_SERVICE = "headwayService";
    public static final String DESTINATION_AIMED_ARRIVAL_TIME = "destinationAimedArrivalTime";
    public static final String FIRST_OR_LAST_JOURNEY = "firstOrLastJourney";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyEndTimesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set headwayService
        writeField(writer, HEADWAY_SERVICE, source.headwayService());

        // set originAimedDepartureTime
        writeField(writer, ORIGIN_AIMED_DEPARTURE_TIME, source.originAimedDepartureTime());

        // set destinationAimedArrivalTime
        writeField(writer, DESTINATION_AIMED_ARRIVAL_TIME, source.destinationAimedArrivalTime());

        // set firstOrLastJourney
        Integer firstOrLastJourney = source.firstOrLastJourney();
        if (firstOrLastJourney != null) {
            writeField(writer, FIRST_OR_LAST_JOURNEY, FirstOrLastJourneyEnumeration.values()[firstOrLastJourney]);
        }

    }
}