package siri.xlite.common.csv;

import org.apache.commons.text.StringEscapeUtils;

public class DestinationBuilder extends CsvUtil {

    public static String table() {
        return DESTINATION.class.getSimpleName().toLowerCase();
    }

    public static String columns() {
        return columns(DESTINATION.class);
    }

    @lombok.Builder(builderClassName = "CsvBuilder")
    public static String create(Integer id, String destinationRef, String placeName, String lineRef) {
        return new Builder()
                .append(id)
                .append(DELIMITER)
                .append(destinationRef)
                .append(DELIMITER)
                .append(StringEscapeUtils.escapeCsv(placeName))
                .append(DELIMITER)
                .append(lineRef)
                .append(EOL)
                .toString();
    }

    public enum DESTINATION {
        id, destinationref, placename, line_lineref
    }

}
