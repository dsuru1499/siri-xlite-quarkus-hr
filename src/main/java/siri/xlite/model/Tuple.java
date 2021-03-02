package siri.xlite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(fluent = true)
@Getter
@AllArgsConstructor(staticName = "of")
@ToString
public class Tuple<L extends SiriEntity, R> extends SiriEntity {

    final L left;
    final R right;

    @Override
    public Date recordedAtTime() {
        return left.recordedAtTime();
    }
}
