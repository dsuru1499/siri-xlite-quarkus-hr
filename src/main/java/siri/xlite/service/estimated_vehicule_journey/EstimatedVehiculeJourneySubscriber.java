package siri.xlite.service.estimated_vehicule_journey;

import lombok.extern.slf4j.Slf4j;
import siri.xlite.common.ItemSubscriber;
import siri.xlite.common.marshaller.json.*;
import siri.xlite.model.Call;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.*;

@Slf4j
public class EstimatedVehiculeJourneySubscriber
        extends ItemSubscriber<VehicleJourney, EstimatedVehiculeJourneyParameters> {

    private static final String EXTRA_CALL = "extraCall";
    private static final String CANCELLATION = "cancellation";
    private static final String ESTIMATED_CALLS = "estimatedCalls";

    @Override
    protected void writeItem(VehicleJourney t) {
        writeObject(writer, t, source -> {
            LineIdentityGroupMarshaller.getInstance().write(writer, source);
            EstimatedTimetableAlterationGroupMarshaller.getInstance().write(writer, source);
            JourneyPatternInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndNamesGroupMarshaller.getInstance().write(writer, source);
            ServiceInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndTimesGroupMarshaller.getInstance().write(writer, source);
            DisruptionGroupMarshaller.getInstance().write(writer, source);
            JourneyProgressGroupMarshaller.getInstance().write(writer, source);
            TrainOperationalInfoGroupMarshaller.getInstance().write(writer, source);

            // EstimatedCalls calls
            writeArray(writer, ESTIMATED_CALLS, source.calls(), this::writeEstimatedCall);
        });
    }

    private void writeEstimatedCall(Object o) {
        if (o instanceof Call) {
            Call call = (Call) o;
            writeObject(writer, call, source -> {
                StopPointInSequenceGroupMarshaller.getInstance().write(writer, source);

                // setExtraCall
                writeField(writer, EXTRA_CALL, source.extraCall());

                // cancellation
                writeField(writer, CANCELLATION, source.cancellation());

                // CallRealTimeInfoGroupMarshaller.getInstance().write(writer, source);
                CallPropertyGroupMarshaller.getInstance().write(writer, source);
                // CallNoteGroupMarshaller.getInstance().write(writer, source);
                DisruptionGroupMarshaller.getInstance().write(writer, source);
                OnwardVehicleArrivalTimesGroupMarshaller.getInstance().write(writer, source);
                MonitoredStopArrivalStatusGroupMarshaller.getInstance().write(writer, source);
                OnwardVehicleDepartureTimesGroupMarshaller.getInstance().write(writer, source);
                // PassengerDepartureTimesGroupMarshaller.getInstance().write(writer, source);
                MonitoredStopDepartureStatusGroupMarshaller.getInstance().write(writer, source);
                HeadwayIntervalGroupMarshaller.getInstance().write(writer, source);
                StopProximityGroupMarshaller.getInstance().write(writer, source);
            });
        }
    }
}