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
import java.util.*;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(name = "vehiclejourney_line_lineref_idx", columnList = "line_lineref")})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VehicleJourney extends SiriEntity {

    @Column(name = "recordedattime")
    private Date recordedAtTime;

    // * EstimatedVehicleJourney

    // * LineIdentityGroup
    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;
    // lineRef :string;
    @Column(name = "directionref")
    private String directionRef;
    // framedVehicleJourneyRef :string;
    // datedVehicleJourneyCode :string;

    // * EstimatedTimetableAlterationGroup
    @Id
    @Column(name = "datedvehiclejourneyref")
    private String datedVehicleJourneyRef;
    // datedVehicleJourneyIndirectRef :string;
    // estimatedVehicleJourneyCode :string;
    @Column(name = "extrajourney")
    private Boolean extraJourney;
    @Column(name = "cancellation")
    private Boolean cancellation;

    // * JourneyPatternInfoGroup
    @Column(name = "journeypatternref")
    private String journeyPatternRef;
    @Column(name = "journeypatternname")
    private String journeyPatternName;
    @Column(name = "vehiclemodes")
    private Integer vehicleModes;
    @Column(name = "routeref")
    private String routeRef;
    @Column(name = "publishedlinename")
    private String publishedLineName;
    // groupOfLinesRef :string;
    @Column(name = "directionname")
    private String directionName;
    // externalLineRef :string;

    // * VehicleJourneyInfoGroup
    @Column(name = "originref")
    private String originRef;
    @Column(name = "originname")
    private String originName;
    // originShortName :string;
    // destinationDisplayAtOrigin :string;
    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "vehicleJourney", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Via> vias = new LinkedHashSet<>();
    @Column(name = "destinationref")
    private String destinationRef;
    @Column(name = "destinationname")
    private String destinationName;
    // destinationShortName :string;
    // originDisplayAtDestination :string;
    @Column(name = "operatorref")
    private String operatorRef;
    @Column(name = "productcategoryref")
    private String productCategoryRef;
    @Convert(converter = StringSetConverter.class)
    @Column(name = "servicefeaturerefs", columnDefinition = "text")
    private Set<String> serviceFeatureRefs = new LinkedHashSet<>();
    @Convert(converter = StringSetConverter.class)
    @Column(name = "vehiclefeaturerefs", columnDefinition = "text")
    private Set<String> vehicleFeatureRefs = new LinkedHashSet<>();
    @Column(name = "vehiclejourneyname")
    private String vehicleJourneyName;
    @Convert(converter = StringSetConverter.class)
    @Column(name = "journeynotes", columnDefinition = "text")
    private Set<String> journeyNotes = new LinkedHashSet<>();
    // publicContact :SimpleContact;
    // operationsContact:SimpleContact;
    @Column(name = "headwayservice")
    private Boolean headwayService;
    @Column(name = "originaimeddeparturetime")
    private Time originAimedDepartureTime;
    @Column(name = "destinationaimedarrivaltime")
    private Time destinationAimedArrivalTime;
    @Column(name = "firstorlastjourney")
    private Integer firstOrLastJourney;

    // ! denormalisation
    @Column(name = "originexpecteddeparturetime")
    private Time originExpectedDepartureTime;
    @Column(name = "destinationexpectedarrivaltime")
    private Time destinationExpectedArrivalTime;

    // * DisruptionGroup
    // facilityConditionElement :[FacilityCondition];
    // facilityChangeElement :[FacilityChange];
    @Convert(converter = StringSetConverter.class)
    @Column(name = "situationrefs", columnDefinition = "text")
    private Set<String> situationRefs = new LinkedHashSet<>();

    // * JourneyProgressGroup
    @Column(name = "monitored")
    private Boolean monitored;
    @Column(name = "monitoringerror")
    private String monitoringError;
    @Column(name = "incongestion")
    private Boolean inCongestion;
    @Column(name = "inpanic")
    private Boolean inPanic;
    // predictionInaccurate :bool;
    // dataSource :string;
    // confidenceLevel :string;
    @Embedded
    private Location vehicleLocation;
    // locationRecordedAtTime :long;
    @Column(name = "bearing")
    private Double bearing;
    // progressRate :string;
    // velocity : long;
    // engineOn :bool;
    @Column(name = "occupancy")
    private Integer occupancy;
    @Column(name = "delay")
    private Long delay;
    // progressStatus :[string];
    // vehicleStatus : string;

    // * TrainOperationalInfoGroup
    // trainBlockPart : [TrainBlockPart];
    // blockRef :string;
    // courseOfJourneyRef :string;
    // vehicleJourneyRef :string;
    // vehicleRef :string;
    // additionalVehicleJourneyRef :[string];
    // driverRef :string;
    // driverName :string;
    @Convert(converter = StringSetConverter.class)
    @Column(name = "trainnumbers", columnDefinition = "text")
    private Set<String> trainNumbers = new LinkedHashSet<>();
    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "vehicleJourney", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JourneyPart> journeyParts = new LinkedHashSet<>();
    // isCompleteStopSequence :bool;

    // * MonitoredVehicleJourney
    // * DatedVehicleJourney
    @Column(name = "origindisplay")
    private String originDisplay;
    @Column(name = "destinationdisplay")
    private String destinationDisplay;
    // lineNote :[string];

    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "vehicleJourney", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("index")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Call> calls = new ArrayList<>();

    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "distributorVehicleJourney", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<siri.xlite.model.TargetedInterchange> targetedInterchanges = new LinkedHashSet<>();

    public static VehicleJourney of() {
        return new VehicleJourney();
    }

    public void addVia(Via via) {
        via.vehicleJourney(this);
        vias.add(via);
    }

    public void removeVia(Via via) {
        vias.remove(via);
        via.vehicleJourney(null);
    }

    public void addJourneyPart(JourneyPart journeyPart) {
        journeyPart.vehicleJourney(this);
        journeyParts.add(journeyPart);
    }

    public void removeJourneyPart(JourneyPart journeyPart) {
        journeyParts.remove(journeyPart);
        journeyPart.vehicleJourney(null);
    }

    public void addCall(Call call) {
        call.vehicleJourney(this);
        calls.add(call);
    }

    public void removeCall(Call call) {
        calls.remove(call);
        call.vehicleJourney(null);
    }

    public void addTargetedInterchange(siri.xlite.model.TargetedInterchange targetedInterchange) {
        targetedInterchange.distributorVehicleJourney(this);
        targetedInterchanges.add(targetedInterchange);
    }

    public void removeTargetedInterchange(siri.xlite.model.TargetedInterchange targetedInterchange) {
        targetedInterchanges.remove(targetedInterchange);
        targetedInterchange.distributorVehicleJourney(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof VehicleJourney))
            return false;
        final VehicleJourney other = (VehicleJourney) o;
        return datedVehicleJourneyRef != null && datedVehicleJourneyRef.equals(other.datedVehicleJourneyRef);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(datedVehicleJourneyRef);
    }

}