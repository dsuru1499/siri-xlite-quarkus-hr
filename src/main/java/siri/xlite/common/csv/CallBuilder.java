package siri.xlite.common.csv;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.Time;
import java.util.Set;

public class CallBuilder extends CsvUtil {

    public static String table() {
        return CALL.class.getSimpleName().toLowerCase();
    }

    public static String columns() {
        return columns(CALL.class);
    }

    @lombok.Builder(builderClassName = "CsvBuilder")
    public static String create(
            Integer id,
            Boolean extraCall,
            Boolean cancellation,
            String stopPointRef,
            Integer order,
            Integer index,
            String originDisplay,
            String destinationDisplay,
            Set<String> situationRefs,
            Time aimedArrivalTime,
            Time actualDepartureTime,
            Time expectedArrivalTime,
            Integer arrivalStatus,
            String arrivalProximityText,
            String arrivalPlatformName,
            Time aimedDepartureTime,
            Time expectedDepartureTime,
            Time actualArrivalTime,
            Integer departureStatus,
            String departurePlatformName,
            Integer departureBoardingActivity,
            Long aimedHeadwayInterval,
            Long expectedHeadwayInterval,
            Long distanceFromStop,
            Long numberOfStopsAway,
            Boolean vehicleAtStop,
            Boolean platformTraversal,
            String datedVehicleJourneyRef
    ) {
        return new Builder()
                .append(id)
                .append(DELIMITER)
                .append(extraCall)
                .append(DELIMITER)
                .append(cancellation)
                .append(DELIMITER)
                .append(stopPointRef)
                .append(DELIMITER)
                .append(order)
                .append(DELIMITER)
                .append(index)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(originDisplay))
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(destinationDisplay))
                .append(DELIMITER)
                .append(CollectionUtils.isNotEmpty(situationRefs) ? String.join(DELIMITER, situationRefs) : null)
                .append(DELIMITER)
                .append((aimedArrivalTime != null) ? TIME_FORMATER.format(aimedArrivalTime) : null)
                .append(DELIMITER)
                .append((actualDepartureTime != null) ? TIME_FORMATER.format(actualDepartureTime) : null)
                .append(DELIMITER)
                .append((expectedArrivalTime != null) ? TIME_FORMATER.format(expectedArrivalTime) : null)
                .append(DELIMITER)
                .append(arrivalStatus)
                .append(DELIMITER)
                .append(arrivalProximityText)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(arrivalPlatformName))
                .append(DELIMITER)
                .append((aimedDepartureTime != null) ? TIME_FORMATER.format(aimedDepartureTime) : null)
                .append(DELIMITER)
                .append((expectedDepartureTime != null) ? TIME_FORMATER.format(expectedDepartureTime) : null)
                .append(DELIMITER)
                .append((actualArrivalTime != null) ? TIME_FORMATER.format(actualArrivalTime) : null)
                .append(DELIMITER)
                .append(departureStatus)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(departurePlatformName))
                .append(DELIMITER)
                .append(departureBoardingActivity)
                .append(DELIMITER)
                .append(aimedHeadwayInterval)
                .append(DELIMITER)
                .append(expectedHeadwayInterval)
                .append(DELIMITER)
                .append(distanceFromStop)
                .append(DELIMITER)
                .append(numberOfStopsAway)
                .append(DELIMITER)
                .append(vehicleAtStop)
                .append(DELIMITER)
                .append(platformTraversal)
                .append(DELIMITER)
                .append(datedVehicleJourneyRef)
                .append(EOL)
                .toString();
    }

    public enum CALL {
        id,
        extracall,
        cancellation,
        stoppoint_stoppointref,
        sequence,
        index,
        origindisplay,
        destinationdisplay,
        situationrefs,
        aimedarrivaltime,
        actualdeparturetime,
        expectedarrivaltime,
        arrivalstatus,
        arrivalproximitytext,
        arrivalplatformname,
        aimeddeparturetime,
        expecteddeparturetime,
        actualarrivaltime,
        departurestatus,
        departureplatformname,
        departureboardingactivity,
        aimedheadwayinterval,
        expectedheadwayinterval,
        distancefromstop,
        numberofstopsaway,
        vehicleatstop,
        platformtraversal,
        vehiclejourney_datedvehiclejourneyref
    }

}