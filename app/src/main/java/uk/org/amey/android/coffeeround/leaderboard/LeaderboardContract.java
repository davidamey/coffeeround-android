package uk.org.amey.android.coffeeround.leaderboard;

import java.util.List;

import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.BaseView;
import uk.org.amey.android.coffeeround.data.Person;

public interface LeaderboardContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showLeaders(List<Person> people);

        void showAddRound();

        void showLoadingLeaderboardError();

        void showNoLeaders();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadLeaderboard(boolean forceUpdate);

    }

}
