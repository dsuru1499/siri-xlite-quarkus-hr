package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.Call;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.*;

public class DisruptionGroupMarshaller implements Marshaller<Object> {

    public static final String SITUATION_REFS = "situationRefs";
    public static final String SITUATION_SIMPLE_REF = "situationSimpleRef";

    @Getter
    private static final Marshaller<Object> instance = new DisruptionGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, Object source) {
        if (source instanceof VehicleJourney) {
            write(writer, (VehicleJourney) source);
        } else if (source instanceof Call) {
            write(writer, (Call) source);
        }
    }

    private void write(JsonGenerator writer, VehicleJourney source) {
        // facilityConditionElement :[FacilityCondition];
        // facilityChangeElement :[FacilityChange];
        // set situationRef
        writeArray(writer, SITUATION_REFS, source.situationRefs(),
                t -> writeObject(writer, t, situationRef -> writeField(writer, SITUATION_SIMPLE_REF, situationRef)));

    }

    private void write(JsonGenerator writer, Call source) {
        // facilityConditionElement :[FacilityCondition];
        // facilityChangeElement :[FacilityChange];
        // set situationRef
        writeArray(writer, SITUATION_REFS, source.situationRefs(),
                t -> writeObject(writer, t, situationRef -> writeField(writer, SITUATION_SIMPLE_REF, situationRef)));
    }
}
