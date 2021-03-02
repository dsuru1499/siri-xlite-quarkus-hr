package siri.xlite.common;

import io.reactivex.exceptions.Exceptions;
import lombok.extern.slf4j.Slf4j;
import siri.xlite.model.SiriEntity;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static siri.xlite.common.JsonUtils.writeEndDocument;
import static siri.xlite.common.JsonUtils.writeStartDocument;

@Slf4j
public abstract class ItemSubscriber<T extends SiriEntity, P extends DefaultParameters> extends SiriSubscriber<T, P> {

    protected final AtomicInteger count = new AtomicInteger();
    protected T current;

    @Override
    public void onNext(T source) {
        try {
            count.incrementAndGet();
            this.current = source;
            writeStartDocument(writer, parameters);
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
                writeEndDocument(writer);
                writeResponse(getLastModified());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    public Date getLastModified() {
        return siri.xlite.common.CacheControl.getLastModified(current);
    }

    protected abstract void writeItem(T t);

}