package siri.xlite.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Time;
import java.util.LinkedHashSet;
import java.util.Set;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(name = "call_stoppoint_stoppointref_idx", columnList = "stoppoint_stoppointref"),
        @Index(name = "call_vehiclejourney_datedvehiclejourneyref_idx", columnList = "vehiclejourney_datedvehiclejourneyref")})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Call extends siri.xlite.model.BaseEntity {

    @Id
    @GeneratedValue(generator = "call_seq")
    @SequenceGenerator(name = "call_seq", allocationSize = 100)
    @Column(name = "id")
    private Integer id;

    // * EstimatedCallStructure
    @Column(name = "extracall")
    private Boolean extraCall;
    @Column(name = "cancellation")
    private Boolean cancellation;

    // * StopPointInSequenceGroup
    // stopPointRef :string;
    @ManyToOne(fetch = FetchType.LAZY)
    private siri.xlite.model.StopPoint stopPoint;
    // visitNumber :ushort;

    @Column(name = "sequence")
    private Integer order;
    @Column(name = "index")
    private Integer index;
    // stopPointName :string;

    // * CallRealTimeInfoGroup
    // predictionInaccurate :bool;
    // occupancy :byte;

    // * CallPropertyGroup
    // timingPoint :bool;
    // boardingStretch :bool;
    // requestStop :bool;
    @Column(name = "origindisplay")
    private String originDisplay;
    @Column(name = "destinationDisplay")
    private String destinationDisplay;

    // * CallNoteGroup
    // callNote :[string];

    // * DisruptionGroup
    // facilityConditionElement: [FacilityCondition];
    // facilityChangeElement :FacilityChange;
    @Convert(converter = StringSetConverter.class)
    @Column(name = "situationrefs", columnDefinition = "text")
    private Set<String> situationRefs = new LinkedHashSet<>();

    // * OnwardVehicleArrivalTimesGroup VehicleArrivalTimesGroup, MonitoredCallArrivalTimesGroup
    @Column(name = "aimedarrivaltime")
    private Time aimedArrivalTime;
    @Column(name = "actualdeparturetime")
    private Time actualDepartureTime;
    @Column(name = "expectedarrivaltime")
    private Time expectedArrivalTime;
    // expectedArrivalPredictionQuality :PredictionQuality;
    // latestExpectedArrivalTime :long;

    // * MonitoredStopArrivalStatusGroup
    @Column(name = "arrivalstatus")
    private Integer arrivalStatus;
    @Column(name = "arrivalproximitytext")
    private String arrivalProximityText;
    @Column(name = "arrivalplatformname")
    private String arrivalPlatformName;
    // arrivalBoardingActivity :byte;
    // arrivalStopAssignment :StopAssignment;
    // arrivalOperatorRefs :[string];

    // * OnwardVehicleDepartureTimesGroup VehicleDepartureTimesGroup, MonitoredCallDepartureTimesGroup
    @Column(name = "aimeddeparturetime")
    private Time aimedDepartureTime;
    @Column(name = "expecteddeparturetime")
    private Time expectedDepartureTime;
    // provisionalExpectedDepartureTime :long;
    // earliestExpectedDepartureTime :long;
    // expectedDeparturePredictionQuality :PredictionQuality;
    @Column(name = "actualarrivaltime")
    private Time actualArrivalTime;

    // * PassengerDepartureTimesGroup
    // aimedLatestPassengerAccessTime :long;
    // expectedLatestPassengerAccessTime :long;

    // * MonitoredStopDepartureStatusGroup
    @Column(name = "departurestatus")
    private Integer departureStatus;
    // departureProximityText :string;
    @Column(name = "departureplatformname")
    private String departurePlatformName;
    @Column(name = "departureboardingactivity")
    private Integer departureBoardingActivity;
    // departureStopAssignment :StopAssignment;
    // departureOperatorRefs :[string];

    // * HeadwayIntervalGroup
    @Column(name = "aimedheadwayinterval")
    private Long aimedHeadwayInterval;
    @Column(name = "expectedheadwayinterval")
    private Long expectedHeadwayInterval;

    // * StopProximityGroup
    @Column(name = "distancefromstop")
    private Long distanceFromStop;
    @Column(name = "numberofstopsaway")
    private Long numberOfStopsAway;

    // * MonitoredCallStructure

    // * CallRealtimeGroup
    @Column(name = "vehicleatstop")
    private Boolean vehicleAtStop;
    // vehicleLocationAtStop : Location;

    // * CallRailGroup
    // reversesAtStop :bool;
    @Column(name = "platformtraversal")
    private Boolean platformTraversal;
    // signalStatus :string;

    // DatedCallStructure
    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "call", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<siri.xlite.model.TargetedInterchange> targetedInterchanges = new LinkedHashSet<>();
    // fromServiceJourneyInterchange :[ServiceJourneyInterchange];
    // toServiceJourneyInterchange :[ServiceJourneyInterchange];

    @ManyToOne(fetch = FetchType.LAZY)
    private siri.xlite.model.VehicleJourney vehicleJourney;

    public static Call of() {
        return new Call();
    }

    public void addTargetedInterchange(siri.xlite.model.TargetedInterchange targetedInterchange) {
        targetedInterchange.call(this);
        targetedInterchanges.add(targetedInterchange);
    }

    public void removeTargetedInterchange(siri.xlite.model.TargetedInterchange targetedInterchange) {
        targetedInterchanges.remove(targetedInterchange);
        targetedInterchange.call(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Call other = (Call) o;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
