package siri.xlite.model;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class SiriEntity extends siri.xlite.model.BaseEntity {

    public abstract Date recordedAtTime();
}
