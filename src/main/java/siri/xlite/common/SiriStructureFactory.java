package siri.xlite.common;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class SiriStructureFactory {

    public static String createXMLGregorianCalendar() {
        return createXMLGregorianCalendar(LocalDateTime.now());
    }

    public static String createXMLGregorianCalendar(long epochMilli) {
        LocalDateTime dateTime = Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return createXMLGregorianCalendar(dateTime);
    }

    public static String createXMLGregorianCalendar(Date date) {
        return createXMLGregorianCalendar(DateTimeUtils.toLocalDateTime(date));
    }

    private static String createXMLGregorianCalendar(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String createParticipantRef(String producerDomain, String producerName) {
        return producerDomain + ":" + producerName;
    }

    public static String createMessageIdentifier() {
        return UUID.randomUUID().toString();
    }

    public static String createDuration(Long delay) {
        String result = null;
        if (delay != null) {
            result = Duration.ofMillis(delay).toString();
        }
        return result;
    }

}
