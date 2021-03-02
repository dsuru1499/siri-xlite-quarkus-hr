package siri.xlite.common.csv;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.sql.Time;
import java.util.Date;
import java.util.Set;

public class VehicleJourneyBuilder extends CsvUtil {

    public static String table() {
        return VEHICLEJOURNEY.class.getSimpleName().toLowerCase();
    }

    public static String columns() {
        return columns(VEHICLEJOURNEY.class);
    }

    @lombok.Builder(builderClassName = "CsvBuilder")
    public static String create(
            Date recordedAtTime,
            String lineRef,
            String directionRef,
            String datedVehicleJourneyRef,
            Boolean extraJourney,
            Boolean cancellation,
            String journeyPatternRef,
            String journeyPatternName,
            Integer vehicleModes,
            String routeRef,
            String publishedLineName,
            String directionName,
            String originRef,
            String originName,
            String destinationRef,
            String destinationName,
            String operatorRef,
            String productCategoryRef,
            Set<String> serviceFeatureRefs,
            Set<String> vehicleFeatureRefs,
            String vehicleJourneyName,
            Set<String> journeyNotes,
            Boolean headwayService,
            Time originAimedDepartureTime,
            Time destinationAimedArrivalTime,
            Integer firstOrLastJourney,
            Time originExpectedDepartureTime,
            Time destinationExpectedArrivalTime,
            Set<String> situationRefs,
            Boolean monitored,
            String monitoringError,
            Boolean inCongestion,
            Boolean inPanic,
            Double longitude,
            Double latitude,
            Double bearing,
            Integer occupancy,
            Long delay,
            Set<String> trainNumbers,
            String originDisplay,
            String destinationDisplay
    ) {
        return new Builder()
                .append((recordedAtTime != null) ? DATETIME_FORMATER.format(recordedAtTime) : null)
                .append(DELIMITER)
                .append(lineRef)
                .append(DELIMITER)
                .append(directionRef)
                .append(DELIMITER)
                .append(datedVehicleJourneyRef)
                .append(DELIMITER)
                .append(extraJourney)
                .append(DELIMITER)
                .append(cancellation)
                .append(DELIMITER)
                .append(journeyPatternRef)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(journeyPatternName))
                .append(DELIMITER)
                .append(vehicleModes)
                .append(DELIMITER)
                .append(routeRef)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(publishedLineName))
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(directionName))
                .append(DELIMITER)
                .append(originRef)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(originName))
                .append(DELIMITER)
                .append(destinationRef)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(destinationName))
                .append(DELIMITER)
                .append(operatorRef)
                .append(DELIMITER)
                .append(productCategoryRef)
                .append(DELIMITER)
                .append(CollectionUtils.isNotEmpty(serviceFeatureRefs) ? String.join(DELIMITER, serviceFeatureRefs) : null)
                .append(DELIMITER)
                .append(CollectionUtils.isNotEmpty(vehicleFeatureRefs) ? String.join(DELIMITER, vehicleFeatureRefs) : null)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(vehicleJourneyName))
                .append(DELIMITER)
                .append(CollectionUtils.isNotEmpty(journeyNotes) ? StringEscapeUtils.escapeCsv(String.join(DELIMITER, journeyNotes)) : null)
                .append(DELIMITER)
                .append(headwayService)
                .append(DELIMITER)
                .append((originAimedDepartureTime != null) ? TIME_FORMATER.format(originAimedDepartureTime) : null)
                .append(DELIMITER)
                .append((destinationAimedArrivalTime != null) ? TIME_FORMATER.format(destinationAimedArrivalTime) : null)
                .append(DELIMITER)
                .append(firstOrLastJourney)
                .append(DELIMITER)
                .append((originExpectedDepartureTime != null) ? TIME_FORMATER.format(originExpectedDepartureTime) : null)
                .append(DELIMITER)
                .append((destinationExpectedArrivalTime != null) ? TIME_FORMATER.format(destinationExpectedArrivalTime) : null)
                .append(DELIMITER)
                .append(CollectionUtils.isNotEmpty(situationRefs) ? StringEscapeUtils.escapeCsv(String.join(DELIMITER, situationRefs)) : null)
                .append(DELIMITER)
                .append(monitored)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(monitoringError))
                .append(DELIMITER)
                .append(inCongestion)
                .append(DELIMITER)
                .append(inPanic)
                .append(DELIMITER)
                .append(longitude)
                .append(DELIMITER)
                .append(latitude)
                .append(DELIMITER)
                .append(bearing)
                .append(DELIMITER)
                .append(occupancy)
                .append(DELIMITER)
                .append(delay)
                .append(DELIMITER)
                .append(CollectionUtils.isNotEmpty(trainNumbers) ? StringEscapeUtils.escapeCsv(String.join(DELIMITER, trainNumbers)) : null)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(originDisplay))
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(destinationDisplay))
                .append(EOL)
                .toString();
    }

    public enum VEHICLEJOURNEY {
        recordedattime,
        line_lineref,
        directionref,
        datedvehiclejourneyref,
        extrajourney,
        cancellation,
        journeypatternref,
        journeypatternname,
        vehiclemodes,
        routeref,
        publishedlinename,
        directionname,
        originref,
        originname,
        destinationref,
        destinationname,
        operatorref,
        productcategoryref,
        servicefeaturerefs,
        vehiclefeaturerefs,
        vehiclejourneyname,
        journeynotes,
        headwayservice,
        originaimeddeparturetime,
        destinationaimedarrivaltime,
        firstorlastjourney,
        originexpecteddeparturetime,
        destinationexpectedarrivaltime,
        situationrefs,
        monitored,
        monitoringerror,
        incongestion,
        inpanic,
        longitude,
        latitude,
        bearing,
        occupancy,
        delay,
        trainnumbers,
        origindisplay,
        destinationdisplay
    }

}