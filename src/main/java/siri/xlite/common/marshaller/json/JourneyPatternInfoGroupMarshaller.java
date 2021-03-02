package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeField;

public class JourneyPatternInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String ROUTE_REF = "routeRef";
    public static final String JOURNEY_PATTERN_REF = "journeyPatternRef";
    public static final String JOURNEY_PATTERN_NAME = "journeyPatternName";
    public static final String VEHICLE_MODES = "vehicleModes";
    public static final String PUBLISHED_LINE_NAME = "publishedLineName";
    public static final String DIRECTION_NAME = "directionName";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyPatternInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set journeyPatternRef
        writeField(writer, JOURNEY_PATTERN_REF, source.journeyPatternRef());

        // set journeyPatternName
        writeField(writer, JOURNEY_PATTERN_NAME, source.journeyPatternName());

        // set vehicleMode
        writeField(writer, VEHICLE_MODES, source.vehicleModes());

        // set routeRef
        writeField(writer, ROUTE_REF, source.routeRef());

        // set publishedLineName
        writeField(writer, PUBLISHED_LINE_NAME, source.publishedLineName());

        // groupOfLinesRef :string;

        // set directionName
        writeField(writer, DIRECTION_NAME, source.directionName());

        // externalLineRef :string;
    }

}
