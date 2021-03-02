package siri.xlite.common.csv;

import org.apache.commons.text.StringEscapeUtils;

import java.util.Date;


public class LineBuilder extends CsvUtil {

    public static String table() {
        return LINE.class.getSimpleName().toLowerCase();
    }

    public static String columns() {
        return columns(LineBuilder.LINE.class);
    }

    @lombok.Builder(builderClassName = "CsvBuilder")
    public static String create(Date recordedAtTime, String lineRef, String lineName, Boolean monitored) {
        return new Builder()
                .append(DATETIME_FORMATER.format(recordedAtTime))
                .append(DELIMITER)
                .append(lineRef)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(lineName))
                .append(DELIMITER)
                .append(monitored)
                .append(EOL)
                .toString();
    }

    public enum LINE {
        recordedattime, lineref, linename, monitored,
    }

}
