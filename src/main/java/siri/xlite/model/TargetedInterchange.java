package siri.xlite.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class TargetedInterchange extends SiriEntity {

    @Column(name = "recordedattime")
    private Date recordedAtTime;

    @Id
    @GeneratedValue(generator = "interchange_seq")
    @SequenceGenerator(name = "interchange_seq", allocationSize = 100)
    @Column(name = "interchangecode")
    private String interchangeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleJourney distributorVehicleJourney;

    // distributorConnectionLinkRef ;
    @Column(name = "connectioncode")
    private String connectionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private StopPoint stopPoint;

    @Column(name = "interchangeduration")
    private Long interchangeDuration;
    @Column(name = "frequenttravellerduration")
    private Long frequentTravellerDuration;
    @Column(name = "occasionaltravellerduration")
    private Long occasionalTravellerDuration;
    @Column(name = "impairedaccessduration")
    private Long impairedAccessDuration;

    // distributorVisitNumber :long;
    // distributorOrder :long;
    @Column(name = "stayseated")
    private Boolean staySeated;
    @Column(name = "guaranteed")
    private Boolean guaranteed;
    // advertised :bool;
    // standardWaitTime :long;
    @Column(name = "maximumwaittime")
    private Long maximumWaitTime;
    // maximumAutomaticWaitTime :long;
    // standardTransferTime :long;
    // minimumTransferTime :long;
    // maximumTransferTime :long;

    @ManyToOne(fetch = FetchType.LAZY)
    private Call call;

    public static TargetedInterchange of() {
        return new TargetedInterchange();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TargetedInterchange))
            return false;
        final TargetedInterchange other = (TargetedInterchange) o;
        return interchangeCode != null && interchangeCode.equals(other.interchangeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(interchangeCode);
    }

}
