package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeArray;
import static siri.xlite.common.JsonUtils.writeField;

public class JourneyEndNamesGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String DESTINATION_REF = "destinationRef";
    public static final String ORIGIN_REF = "originRef";
    public static final String ORIGIN_NAME = "originName";
    public static final String VIAS = "vias";
    public static final String PLACE_REF = "placeRef";
    public static final String PLACE_NAME = "placeName";
    public static final String DESTINATION_NAME = "destinationName";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyEndNamesGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set originRef
        writeField(writer, ORIGIN_REF, source.originRef());

        // set originName
        writeField(writer, ORIGIN_NAME, source.originName());

        // originShortName :string;
        // destinationDisplayAtOrigin :string;

        // set via
        writeArray(writer, VIAS, source.vias(), o -> {

            // set placeRef
            writeField(writer, PLACE_REF, o.placeRef());

            // set placeName
            writeField(writer, PLACE_NAME, o.placeName());
        });

        // set destinationRef
        writeField(writer, DESTINATION_REF, source.destinationRef());

        // set destinationName
        writeField(writer, DESTINATION_NAME, source.destinationName());

        // destinationShortName :string;
        // ? originDisplayAtDestination :string;

    }
}
