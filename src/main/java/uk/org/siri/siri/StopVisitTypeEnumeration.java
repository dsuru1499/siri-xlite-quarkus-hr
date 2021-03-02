package uk.org.siri.siri;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour StopVisitTypeEnumeration.
 *
 * <p>
 * Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="StopVisitTypeEnumeration"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="all"/&gt;
 *     &lt;enumeration value="arrivals"/&gt;
 *     &lt;enumeration value="departures"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "StopVisitTypeEnumeration")
@XmlEnum
public enum StopVisitTypeEnumeration {

    /**
     * Return all Stop Visits.
     */
    @XmlEnumValue("all")
    ALL("all"),

    /**
     * Return only arrival Stop Visits.
     */
    @XmlEnumValue("arrivals")
    ARRIVALS("arrivals"),

    /**
     * Return only departure Stop Visits.
     */
    @XmlEnumValue("departures")
    DEPARTURES("departures");
    private final String value;

    StopVisitTypeEnumeration(String v) {
        value = v;
    }

    public static StopVisitTypeEnumeration fromValue(String v) {
        for (StopVisitTypeEnumeration c : StopVisitTypeEnumeration.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }

}
