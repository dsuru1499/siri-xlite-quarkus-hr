package uk.org.siri.siri;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour DepartureBoardingActivityEnumeration.
 *
 * <p>
 * Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="DepartureBoardingActivityEnumeration">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="boarding"/>
 *     &lt;enumeration value="noBoarding"/>
 *     &lt;enumeration value="passThru"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "DepartureBoardingActivityEnumeration")
@XmlEnum
public enum DepartureBoardingActivityEnumeration {

    @XmlEnumValue("boarding")
    BOARDING("boarding"), @XmlEnumValue("noBoarding")
    NO_BOARDING("noBoarding"), @XmlEnumValue("passThru")
    PASS_THRU("passThru");
    private final String value;

    DepartureBoardingActivityEnumeration(String v) {
        value = v;
    }

    public static DepartureBoardingActivityEnumeration fromValue(String v) {
        for (DepartureBoardingActivityEnumeration c : DepartureBoardingActivityEnumeration.values()) {
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
