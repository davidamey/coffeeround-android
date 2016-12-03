package uk.org.amey.android.coffeeround.leaderboard;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.org.amey.android.coffeeround.data.CoffeeRoundApi;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.User;

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

        Call<List<User>> call = CoffeeRoundClientGenerator.getClient().getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                view.showLeaderboard(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                view.showLoadingLeaderboardError();
            }
        });
    }

    //endregion
}
