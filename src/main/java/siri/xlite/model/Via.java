package siri.xlite.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {
        @Index(name = "via_vehiclejourney_datedvehiclejourneyref_idx", columnList = "vehiclejourney_datedvehiclejourneyref")})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Via extends siri.xlite.model.BaseEntity {

    @Id
    @GeneratedValue(generator = "via_seq")
    @SequenceGenerator(name = "via_seq", allocationSize = 100)
    @Column(name = "id")
    private Integer id;

    // viaPriority :long;
    @Column(name = "placeref")
    private String placeRef;
    @Column(name = "placeName")
    private String placeName;
    // placeShortName :string;

    @ManyToOne(fetch = FetchType.LAZY)
    private siri.xlite.model.VehicleJourney vehicleJourney;

    public static Via of() {
        return new Via();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Via))
            return false;
        final Via other = (Via) o;
        return Objects.equals(vehicleJourney, other.vehicleJourney()) && Objects.equals(placeRef, other.placeRef());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleJourney, placeRef);
    }

}
