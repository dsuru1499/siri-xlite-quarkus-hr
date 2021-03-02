package uk.org.siri.siri;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour FirstOrLastJourneyEnumeration.
 *
 * <p>
 * Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="FirstOrLastJourneyEnumeration">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="firstServiceOfDay"/>
 *     &lt;enumeration value="otherService"/>
 *     &lt;enumeration value="lastServiceOfDay"/>
 *     &lt;enumeration value="unspecified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "FirstOrLastJourneyEnumeration")
@XmlEnum
public enum FirstOrLastJourneyEnumeration {

    @XmlEnumValue("firstServiceOfDay")
    FIRST_SERVICE_OF_DAY("firstServiceOfDay"), @XmlEnumValue("otherService")
    OTHER_SERVICE("otherService"), @XmlEnumValue("lastServiceOfDay")
    LAST_SERVICE_OF_DAY("lastServiceOfDay"), @XmlEnumValue("unspecified")
    UNSPECIFIED("unspecified");
    private final String value;

    FirstOrLastJourneyEnumeration(String v) {
        value = v;
    }

    public static FirstOrLastJourneyEnumeration fromValue(String v) {
        for (FirstOrLastJourneyEnumeration c : FirstOrLastJourneyEnumeration.values()) {
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
