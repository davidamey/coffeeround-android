package uk.org.amey.android.coffeeround.leaderboard;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.org.amey.android.coffeeround.BasePresenterRx;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.User;

class LeaderboardPresenter extends BasePresenterRx<LeaderboardPresenter.ViewRx> {

    interface ViewRx extends BasePresenterRx.ViewInterface {
        Observable<Void> onRefreshAction();

        void showLoading();
        void hideLoading();
        void showLeaderboard(List<User> users);
        void showLoadingLeaderboardError();
    }

    @Override
    public void register(ViewRx view) {
        super.register(view);

        addToUnsubscribe(view.onRefreshAction()
                .doOnNext(ignored -> view.showLoading())
                .switchMap(ignored -> CoffeeRoundClientGenerator.getClient().getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(throwable -> {
                        view.showLoadingLeaderboardError();
                        return Observable.just(null);
                    })
                )
                .subscribe(users -> {
                    view.showLeaderboard(users);
                    view.hideLoading();
                })
        );
    }
}
