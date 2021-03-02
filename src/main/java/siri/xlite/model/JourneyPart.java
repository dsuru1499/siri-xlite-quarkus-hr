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
        @Index(name = "journeypart_vehiclejourney_datedvehiclejourneyref_idx", columnList = "vehiclejourney_datedvehiclejourneyref")})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JourneyPart {

    @Id
    @GeneratedValue(generator = "journeypart_seq")
    @SequenceGenerator(name = "journeypart_seq", allocationSize = 100)
    @Column(name = "id")
    private Integer id;

    @Column(name = "journeypartref")
    private String journeyPartRef;
    @Column(name = "trainnumberref")
    private String trainNumberRef;
    // operatorRef :string;

    @ManyToOne(fetch = FetchType.LAZY)
    private siri.xlite.model.VehicleJourney vehicleJourney;

    public static JourneyPart of() {
        return new JourneyPart();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof JourneyPart))
            return false;
        final JourneyPart other = (JourneyPart) o;
        return Objects.equals(vehicleJourney, other.vehicleJourney())
                && Objects.equals(journeyPartRef, other.journeyPartRef());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleJourney, journeyPartRef);
    }

}
