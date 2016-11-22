package uk.org.amey.android.coffeeround.leaderboard;

import java.util.List;

import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.BaseView;
import uk.org.amey.android.coffeeround.data.model.User;

public interface LeaderboardContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLeaderboard(List<User> users);

        void showAddRound();

        void showLoadingLeaderboardError();

        void showNoUsers();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadLeaderboard(boolean forceUpdate);

    }

}
