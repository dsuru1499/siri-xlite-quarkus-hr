package siri.xlite.common;

import io.reactivex.exceptions.Exceptions;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.model.SiriEntity;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static siri.xlite.common.JsonUtils.writeEndDocument;
import static siri.xlite.common.JsonUtils.writeStartDocument;

@Slf4j
public abstract class CollectionSubscriber<T extends SiriEntity, P extends DefaultParameters>
        extends SiriSubscriber<T, P> {

    protected final AtomicInteger count = new AtomicInteger();
    protected T current;

    @Override
    public void onNext(T source) {
        try {
            count.incrementAndGet();
            if (count.get() == 1) {
                writeStartDocument(writer, parameters);
                writer.writeStartArray();
            }
            if (current == null || CacheControl.COMPARATOR.compare(source, current) > 0) {
                this.current = source;
            }
            writeItem(source);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                writeNotFound();
            } else {
                writer.writeEndArray();
                writeEndDocument(writer);
                writeResponse(getLastModified());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    public Date getLastModified() {
        return CacheControl.getLastModified(current);
    }

    protected abstract void writeItem(T t);

}