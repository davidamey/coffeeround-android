package uk.org.amey.android.coffeeround.leaderboard;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.User;

class LeaderboardPresenter extends BasePresenter<LeaderboardPresenter.ViewInterface> {

    interface ViewInterface extends BasePresenter.ViewInterface {
        Observable<Void> onRefreshAction();
        Observable<Void> onNewRoundAction();

        void showLoading();
        void hideLoading();
        void showError(String msg);
        
        void showLeaderboard(List<User> users);
        void createNewRound();
    }

    @Override
    public void register(ViewInterface view) {
        super.register(view);

        addToUnsubscribe(view.onRefreshAction()
                .doOnNext(ignored -> view.showLoading())
                .switchMap(ignored -> CoffeeRoundClientGenerator.getClient().getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(throwable -> {
                        view.showError("Unable to get users");
                        return Observable.just(null);
                    })
                )
                .subscribe(users -> {
                    view.showLeaderboard(users);
                    view.hideLoading();
                })
        );

        addToUnsubscribe(view.onNewRoundAction()
                .subscribe(ignored -> view.createNewRound())
        );
    }
}
