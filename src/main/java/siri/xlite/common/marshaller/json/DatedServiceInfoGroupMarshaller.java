package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;
import uk.org.siri.siri.FirstOrLastJourneyEnumeration;

import static siri.xlite.common.JsonUtils.writeField;

public class DatedServiceInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String DESTINATION_DISPLAY = "destinationDisplay";
    public static final String FIRST_OR_LAST_JOURNEY = "firstOrLastJourney";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new DatedServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set destinationDisplay
        writeField(writer, DESTINATION_DISPLAY, source.destinationDisplay());

        // lineNote :[string];

        // set firstOrLastJourney
        Integer firstOrLastJourney = source.firstOrLastJourney();
        if (firstOrLastJourney != null) {
            writeField(writer, FIRST_OR_LAST_JOURNEY, FirstOrLastJourneyEnumeration.values()[firstOrLastJourney]);
        }
    }

}
