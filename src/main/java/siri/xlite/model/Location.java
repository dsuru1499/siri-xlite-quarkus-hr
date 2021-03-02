package siri.xlite.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Accessors(fluent = true)
@Getter
@Setter
@NoArgsConstructor(staticName = "of")
@Embeddable
public class Location {

    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    // coordinates :string;
    // precision :long;
    // id :string;
    // srsName :string;
}
