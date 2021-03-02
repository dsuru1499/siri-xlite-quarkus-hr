package siri.xlite.common.csv;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class CsvUtil {

    public static final String DELIMITER = ",";
    public static final String EOL = "\n";
    public static final String OPEN_BRACKET = "(";
    public static final String CLOSE_BRACKET = ")";

    public static final DateFormat DATETIME_FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat DATET_FORMATER = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat TIME_FORMATER = new SimpleDateFormat("HH:mm:ss");

    public static <E extends Enum<?>> String columns(Class<E> columns) {
        StringBuilder builder = new StringBuilder();
        builder.append(OPEN_BRACKET);
        Enum[] values = columns.getEnumConstants();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);
            if (i < values.length - 1) {
                builder.append(DELIMITER);
            }
        }
        builder.append(CLOSE_BRACKET);
        return builder.toString();
    }


    public static class Builder {

        private StringBuilder builder = new StringBuilder();

        public Builder append(String value) {
            if (value != null) builder.append(value);
            return this;
        }

        public Builder append(Integer value) {
            if (value != null) builder.append(value);
            return this;
        }

        public Builder append(Long value) {
            if (value != null) builder.append(value);
            return this;
        }

        public Builder append(Float value) {
            if (value != null) builder.append(value);
            return this;
        }

        public Builder append(Double value) {
            if (value != null) builder.append(value);
            return this;
        }

        public Builder append(Boolean value) {
            if (value != null) builder.append(value);
            return this;
        }

        public String toString() {
            return builder.toString();
        }
    }

}
