package siri.xlite.service.estimated_timetable;

import io.reactivex.rxjava3.exceptions.Exceptions;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.CollectionSubscriber;
import siri.xlite.model.VehicleJourney;

import java.util.Date;

import static siri.xlite.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;
import static siri.xlite.common.JsonUtils.*;
import static siri.xlite.common.SiriService.SEP;
import static siri.xlite.common.marshaller.json.JourneyEndNamesGroupMarshaller.DESTINATION_REF;
import static siri.xlite.common.marshaller.json.JourneyPatternInfoGroupMarshaller.DIRECTION_NAME;
import static siri.xlite.common.marshaller.json.JourneyPatternInfoGroupMarshaller.ROUTE_REF;
import static siri.xlite.common.marshaller.json.LineIdentityGroupMarshaller.DIRECTION_REF;
import static siri.xlite.common.marshaller.json.LineIdentityGroupMarshaller.LINE_REF;
import static siri.xlite.common.marshaller.json.ServiceInfoGroupMarshaller.OPERATOR_REF;
import static siri.xlite.common.marshaller.json.SiriMarshaller.DESTINATION_EXPECTED_ARRIVAL_TIME;
import static siri.xlite.common.marshaller.json.SiriMarshaller.ORIGIN_EXPECTED_DEPARTURE_TIME;

@Slf4j
public class EstimatedTimetableSubscriber extends CollectionSubscriber<VehicleJourney, EstimatedTimetableParameters> {

    @Override
    protected void writeItem(VehicleJourney t) {
        writeObject(writer, t, source -> {
            HttpServerRequest request = context.request();
            String url = baseURI(request) + SEP + ESTIMATED_VEHICLE_JOURNEY + SEP + source.datedVehicleJourneyRef();
            writeField(writer, HREF, url);

            // metadata
            writeField(writer, LINE_REF, source.line().lineRef());
            writeField(writer, ROUTE_REF, source.routeRef());
            writeField(writer, DIRECTION_REF, source.directionRef());
            writeField(writer, DIRECTION_NAME, source.directionName());
            writeField(writer, DESTINATION_REF, source.destinationRef());
            writeField(writer, OPERATOR_REF, source.operatorRef());
            writeField(writer, ORIGIN_EXPECTED_DEPARTURE_TIME, source.originExpectedDepartureTime());
            writeField(writer, DESTINATION_EXPECTED_ARRIVAL_TIME, source.destinationExpectedArrivalTime());
        });
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                writeNotFound();
            } else {
                writer.writeEndArray();
                writeEndDocument(writer);
                writeResponse(new Date());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

}