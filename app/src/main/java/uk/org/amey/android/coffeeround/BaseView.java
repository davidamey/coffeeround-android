package uk.org.amey.android.coffeeround;

public interface BaseView<T> {

    void setPresenter(T presenter);

    boolean isActive();

}
