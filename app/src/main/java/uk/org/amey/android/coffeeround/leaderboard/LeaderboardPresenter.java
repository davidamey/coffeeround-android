package uk.org.amey.android.coffeeround.leaderboard;

import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;

public class LeaderboardPresenter implements LeaderboardContract.Presenter {

    private final LeaderboardContract.View view;

    private boolean firstLoad = true;

    public LeaderboardPresenter(@NonNull LeaderboardContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    //region Contract methods

    @Override
    public void start() {
        view.showAddRound();
        loadLeaderboard(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadLeaderboard(boolean forceUpdate) {

        CoffeeRoundClientGenerator.getClient().getUsersRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(users -> {
                    view.showLeaderboard(users);
                }, err -> {
                    view.showLoadingLeaderboardError();
                });

    }

    //endregion
}
