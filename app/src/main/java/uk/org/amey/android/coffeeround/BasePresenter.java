package uk.org.amey.android.coffeeround;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends BasePresenter.ViewInterface> {

    protected interface ViewInterface {}

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    protected V view;

    @CallSuper
    public void register(@NonNull final V view) {
        if (this.view != null) {
            throw new IllegalStateException("View " + this.view + "is already attached. Cannot attach " + view);
        }
        this.view = view;
    }

    @CallSuper
    public void unregister() {
        if (view == null) {
            throw new IllegalStateException("View is already detached");
        }
        view = null;
        compositeSubscription.clear();
    }

    @CallSuper
    protected final void addToUnsubscribe(@NonNull final Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}
