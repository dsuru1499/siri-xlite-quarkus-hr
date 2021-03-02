package siri.xlite.service.stop_points_discovery;

import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.CollectionSubscriber;
import siri.xlite.model.StopPoint;

import static siri.xlite.common.JsonUtils.*;

@Slf4j
public class StopPointsDiscoverySubscriber extends CollectionSubscriber<StopPoint, StopPointsDiscoveryParameters> {

    private static final String STOP_POINT_REF = "stopPointRef";
    private static final String STOP_NAME = "stopName";
    private static final String LINE_REFS = "lineRefs";
    private static final String LOCATION = "location";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String LINES = "lines";

    @Override
    protected void writeItem(StopPoint t) {
        writeObject(writer, t, source -> {
            writeField(writer, STOP_POINT_REF, source.stopPointRef());
            writeField(writer, STOP_NAME, source.stopName());
            writeArray(writer, LINES, source.lineRefs(),
                    lineRef -> writeObject(writer, lineRef, (line) -> writeField(writer, LINE_REFS, lineRef)));
            writeObject(writer, LOCATION, source.location(), location -> {
                writeField(writer, LONGITUDE, location.longitude());
                writeField(writer, LATITUDE, location.latitude());
            });
        });
    }

}