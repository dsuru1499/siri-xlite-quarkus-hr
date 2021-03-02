package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeField;

public class TimetableRealtimeInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String MONITORED = "monitored";
    public static final String HEADWAY_SERVICE = "headwayService";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new TimetableRealtimeInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set monitored
        writeField(writer, MONITORED, source.monitored());

        // set headwayService
        writeField(writer, HEADWAY_SERVICE, source.headwayService());

    }

}