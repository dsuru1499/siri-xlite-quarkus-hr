package siri.xlite.service.stop_monitoring;

import io.reactivex.rxjava3.exceptions.Exceptions;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.CollectionSubscriber;
import siri.xlite.model.Call;
import siri.xlite.model.Tuple;
import siri.xlite.model.VehicleJourney;

import java.util.Date;

import static siri.xlite.common.EstimatedVehiculeJourney.ESTIMATED_VEHICLE_JOURNEY;
import static siri.xlite.common.JsonUtils.*;
import static siri.xlite.common.SiriService.HASH;
import static siri.xlite.common.SiriService.SEP;
import static siri.xlite.common.marshaller.json.JourneyEndNamesGroupMarshaller.DESTINATION_REF;
import static siri.xlite.common.marshaller.json.JourneyPatternInfoGroupMarshaller.DIRECTION_NAME;
import static siri.xlite.common.marshaller.json.JourneyPatternInfoGroupMarshaller.ROUTE_REF;
import static siri.xlite.common.marshaller.json.LineIdentityGroupMarshaller.DIRECTION_REF;
import static siri.xlite.common.marshaller.json.LineIdentityGroupMarshaller.LINE_REF;
import static siri.xlite.common.marshaller.json.OnwardVehicleArrivalTimesGroupMarshaller.AIMED_ARRIVAL_TIME;
import static siri.xlite.common.marshaller.json.OnwardVehicleDepartureTimesGroupMarshaller.AIMED_DEPARTURE_TIME;
import static siri.xlite.common.marshaller.json.ServiceInfoGroupMarshaller.OPERATOR_REF;
import static siri.xlite.common.marshaller.json.StopPointInSequenceGroupMarshaller.ORDER;
import static siri.xlite.common.marshaller.json.StopPointInSequenceGroupMarshaller.STOP_POINT_REF;


@Slf4j
public class StopMonitoringSubscriber
        extends CollectionSubscriber<Tuple<VehicleJourney, Integer>, StopMonitoringParameters> {

    @Override
    protected void writeItem(Tuple<VehicleJourney, Integer> t) {
        writeObject(writer, t, u -> {
            HttpServerRequest request = context.request();
            VehicleJourney source = u.left();
            Call call = source.calls().get(u.right());

            String url = baseURI(request) + SEP + ESTIMATED_VEHICLE_JOURNEY + SEP + source.datedVehicleJourneyRef()
                    + HASH + call.index();
            writeField(writer, HREF, url);

            // metadata
            writeField(writer, LINE_REF, source.line().lineRef());
            writeField(writer, ROUTE_REF, source.routeRef());
            writeField(writer, DIRECTION_REF, source.directionRef());
            writeField(writer, DIRECTION_NAME, source.directionName());
            writeField(writer, DESTINATION_REF, source.destinationRef());
            writeField(writer, OPERATOR_REF, source.operatorRef());
            writeField(writer, STOP_POINT_REF, call.stopPoint().stopPointRef());
            writeField(writer, ORDER, call.order());
            writeField(writer, AIMED_DEPARTURE_TIME, call.aimedDepartureTime());
            writeField(writer, AIMED_ARRIVAL_TIME, call.aimedArrivalTime());
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