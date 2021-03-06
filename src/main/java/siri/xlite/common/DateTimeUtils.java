package siri.xlite.common;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtils {

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(GregorianCalendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public static GregorianCalendar toGregorianCalendar(LocalDateTime dateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(dateTime, ZoneId.systemDefault()));
    }

    public static LocalTime toLocalTime(Date date) {
        return LocalTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static Time now() {
        return Time.valueOf(LocalTime.now());
    }

    public static Time toTime(LocalTime time) {
        return Time.valueOf(time);
    }

    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date fromXsdDateTime(String text) {
        return Date.from(fromLocalDateTime(text).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime fromLocalDateTime(String text) {
        return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static long toEpochMilli(Time time) {
        LocalTime localTime = time.toLocalTime();
        return LocalDateTime.now().withHour(localTime.getHour()).withMinute(localTime.getMinute())
                .withSecond(localTime.getSecond()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String toRFC1123(Date date) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(dateTime);
    }

    public static Date fromRFC1123(String text) {
        ZonedDateTime dateTime = ZonedDateTime.parse(text, DateTimeFormatter.RFC_1123_DATE_TIME);
        return Date.from(dateTime.toInstant());
    }
}
