package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Line;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeField;

public class LineIdentityGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String LINE_REF = "lineRef";
    public static final String DIRECTION_REF = "directionRef";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new LineIdentityGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        Line line = source.line();

        // set lineRef
        if (line != null) {
            writeField(writer, LINE_REF, line.lineRef());
        }

        // set directionRef
        writeField(writer, DIRECTION_REF, source.directionRef());
    }
}