package siri.xlite.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(name = "destination_line_lineref_idx", columnList = "line_lineref"),})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Destination implements Serializable {

    @Id
    @GeneratedValue(generator = "destination_seq")
    @SequenceGenerator(name = "destination_seq", allocationSize = 100)
    private Integer id;

    private String destinationRef;
    private String placeName;
    // directionRef :string;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "destination_line_lineref_fkey"))
    private siri.xlite.model.Line line;

    public static Destination of() {
        return new Destination();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Destination))
            return false;
        final Destination other = (Destination) o;
        return Objects.equals(line, other.line()) && Objects.equals(destinationRef, other.destinationRef());
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, destinationRef);
    }

}
