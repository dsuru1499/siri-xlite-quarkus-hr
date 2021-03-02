package siri.xlite.common;

import io.smallrye.mutiny.subscription.UniSubscriber;
import io.smallrye.mutiny.subscription.UniSubscription;
import lombok.AllArgsConstructor;
import org.reactivestreams.Subscriber;

@AllArgsConstructor
public class SubscriberWrapper<T> implements UniSubscriber<T> {

    private Subscriber<T> subscriber;

    @Override
    public void onSubscribe(UniSubscription subscription) {
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void onItem(T item) {
        subscriber.onNext(item);
        subscriber.onComplete();
    }

    @Override
    public void onFailure(Throwable failure) {
        subscriber.onError(failure);
    }
}