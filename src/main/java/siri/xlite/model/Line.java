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
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "line")
@Cacheable
public class Line extends siri.xlite.model.SiriEntity {

    @Column(name = "recordedattime")
    private Date recordedAtTime;

    @Id
    @Column(name = "lineref")
    private String lineRef;
    @Column(name = "linename")
    private String lineName;
    @Column(name = "monitored")
    private Boolean monitored;

    //     @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "destinations")
    private Set<Destination> destinations = new LinkedHashSet<>();

    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<siri.xlite.model.VehicleJourney> vehicleJourneys = new LinkedHashSet<>();

    public static Line of() {
        return new Line();
    }

    public void addVehicleJourney(siri.xlite.model.VehicleJourney vehicleJourney) {
        vehicleJourney.line(this);
        vehicleJourneys.add(vehicleJourney);
    }

    public void removeVehicleJourney(siri.xlite.model.VehicleJourney vehicleJourney) {
        vehicleJourneys.remove(vehicleJourney);
        vehicleJourney.line(null);
    }

    public void addDestination(Destination destination) {
        destination.line(this);
        destinations.add(destination);
    }

    public void removeDestination(Destination destination) {
        destinations.remove(destination);
        destination.line(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Line))
            return false;
        final Line other = (Line) o;
        return lineRef != null && lineRef.equals(other.lineRef);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lineRef);
    }

}
