package uk.org.amey.android.coffeeround.leaderboard;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

public class LeaderboardPresenter implements LeaderboardContract.Presenter {

    private final LeaderboardContract.View view;

    private boolean firstLoad = true;

    public LeaderboardPresenter(@NonNull LeaderboardContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        view.showAddRound();
//        loadLeaderboard(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadLeaderboard(boolean forceUpdate) {

    }
}
