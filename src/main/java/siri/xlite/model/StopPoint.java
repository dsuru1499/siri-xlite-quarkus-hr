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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(name = "stoppoint_parent_idx", columnList = "parent"),
        @Index(name = "stoppoint_longitude_idx", columnList = "longitude"),
        @Index(name = "stoppoint_latitude_idx", columnList = "latitude")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "stoppoint")
@Cacheable
public class StopPoint extends SiriEntity {

    @Column(name = "recordedattime")
    private Date recordedAtTime;

    // timingPoint :bool;
    // monitored :bool;

    @Id
    @Column(name = "stoppointref")
    private String stopPointRef;
    @Column(name = "stopname")
    private String stopName;
    // stopAreaRef :string;

    @Convert(converter = StringSetConverter.class)
    @Column(name = "linerefs", columnDefinition = "text")
    private Set<String> lineRefs = new LinkedHashSet<>();
    // features : [Feature];

    @Embedded
    private Location location;

    // url ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", foreignKey = @ForeignKey(name = "stoppoint_parent_fkey"))
    @Setter(value = AccessLevel.PRIVATE)
    private StopPoint parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(value = AccessLevel.PRIVATE)
    private Set<StopPoint> children = new LinkedHashSet<>();

    @OneToMany(mappedBy = "stopPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(value = AccessLevel.PRIVATE)
    private Set<Call> calls = new LinkedHashSet<>();

    @OneToMany(mappedBy = "stopPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(value = AccessLevel.PRIVATE)
    private Set<TargetedInterchange> targetedInterchanges = new LinkedHashSet<>();

    public static StopPoint of() {
        return new StopPoint();
    }

    public void addChildren(StopPoint child) {
        child.parent(this);
        children.add(child);
    }

    public void removeChildren(StopPoint child) {
        children.remove(child);
        child.parent(null);
    }

    public void addCall(Call call) {
        call.stopPoint(this);
        calls.add(call);
    }

    public void removeCall(Call call) {
        calls.remove(call);
        call.stopPoint(null);
    }

    public void addTargetedInterchange(TargetedInterchange targetedInterchange) {
        targetedInterchange.stopPoint(this);
        targetedInterchanges.add(targetedInterchange);
    }

    public void removeTargetedInterchange(TargetedInterchange targetedInterchange) {
        targetedInterchanges.remove(targetedInterchange);
        targetedInterchange.stopPoint(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StopPoint))
            return false;
        final StopPoint other = (StopPoint) o;
        return stopPointRef != null && stopPointRef.equals(other.stopPointRef);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stopPointRef);
    }

}
