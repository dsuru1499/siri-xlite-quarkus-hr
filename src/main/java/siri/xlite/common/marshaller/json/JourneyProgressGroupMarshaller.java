package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;
import uk.org.siri.siri.OccupancyEnumeration;

import static siri.xlite.common.JsonUtils.writeField;
import static siri.xlite.common.JsonUtils.writeObject;

public class JourneyProgressGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String MONITORED = "monitored";
    public static final String MONITORING_ERROR = "monitoringError";
    public static final String IN_CONGESTION = "inCongestion";
    public static final String IN_PANIC = "inPanic";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String VEHICLE_LOCATION = "vehicleLocation";
    public static final String BEARING = "bearing";
    public static final String OCCUPANCY = "occupancy";
    public static final String DELAY = "delay";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new JourneyProgressGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set monitored
        writeField(writer, MONITORED, source.monitored());

        // set monitoringError
        writeField(writer, MONITORING_ERROR, source.monitoringError());

        // set inCongestion
        writeField(writer, IN_CONGESTION, source.inCongestion());

        // set inPanic
        writeField(writer, IN_PANIC, source.inPanic());

        // predictionInaccurate :bool;
        // dataSource :string;
        // confidenceLevel :string;

        // set vehicleLocation
        writeObject(writer, VEHICLE_LOCATION, source.vehicleLocation(), location -> {
            writeField(writer, LONGITUDE, location.longitude());
            writeField(writer, LATITUDE, location.latitude());
        });

        // ? locationRecordedAtTime :long;

        // set bearing
        writeField(writer, BEARING, source.bearing());

        // progressRate :string;
        // ? velocity : long;
        // ? engineOn :bool;

        // set occupancy
        Integer occupancy = source.occupancy();
        if (occupancy != null) {
            writeField(writer, OCCUPANCY, OccupancyEnumeration.values()[occupancy]);
        }

        // set delay
        writeField(writer, DELAY, source.delay());

        // progressStatus :[string];
        // vehicleStatus : string;

    }
}
