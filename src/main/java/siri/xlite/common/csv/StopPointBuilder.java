package siri.xlite.common.csv;

import org.apache.commons.text.StringEscapeUtils;

import java.util.Collection;
import java.util.Date;

public class StopPointBuilder extends CsvUtil {

    public static String table() {
        return STOPPOINT.class.getSimpleName().toLowerCase();
    }

    public static String columns() {
        return columns(STOPPOINT.class);
    }

    @lombok.Builder(builderClassName = "CsvBuilder")
    public static String create(String stopPointRef,
                                String stopName,
                                Double longitude,
                                Double latitude,
                                Collection<String> lineRefs,
                                String parent,
                                Date recordedAtTime) {
        return new Builder()
                .append(DATETIME_FORMATER.format(recordedAtTime))
                .append(DELIMITER)
                .append(stopPointRef)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(stopName))
                .append(DELIMITER)
                .append(longitude)
                .append(DELIMITER)
                .append(latitude)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(String.join(DELIMITER, lineRefs)))
                .append(DELIMITER)
                .append(parent)
                .append(EOL)
                .toString();
    }

    public enum STOPPOINT {
        recordedattime, stoppointref, stopname, longitude, latitude, linerefs, parent
    }

}