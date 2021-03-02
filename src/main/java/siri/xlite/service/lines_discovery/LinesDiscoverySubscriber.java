package siri.xlite.service.lines_discovery;

import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.CollectionSubscriber;
import siri.xlite.model.Destination;
import siri.xlite.model.Line;

import static siri.xlite.common.JsonUtils.*;

@Slf4j
public class LinesDiscoverySubscriber extends CollectionSubscriber<Line, LinesDiscoveryParameters> {

    private static final String DESTINATIONS = "destinations";
    private static final String LINE_REF = "lineRef";
    private static final String LINE_NAME = "lineName";
    private static final String MONITORED = "monitored";
    private static final String DESTINATION_REF = "destinationRef";
    private static final String PLACE_NAME = "placeName";

    @Override
    protected void writeItem(Line t) {
        writeObject(writer, t, source -> {
            writeField(writer, LINE_REF, source.lineRef());
            writeField(writer, LINE_NAME, source.lineName());
            writeField(writer, MONITORED, source.monitored());
            writeArray(writer, DESTINATIONS, source.destinations(), this::writeDestination);
        });
    }

    private void writeDestination(Destination source) {
        writeObject(writer, source, destination -> {
            writeField(writer, DESTINATION_REF, destination.destinationRef());
            writeField(writer, PLACE_NAME, destination.placeName());
        });
    }

}