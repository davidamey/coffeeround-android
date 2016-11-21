package uk.org.amey.android.coffeeround.leaderboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.Person;

public class LeaderboardFragment extends Fragment implements LeaderboardContract.View {

    private LeaderboardContract.Presenter presenter;

    public LeaderboardFragment() {
        // Requires empty public constructor
    }

    public static LeaderboardFragment newInstance() {
        return new LeaderboardFragment();
    }

    //region Lifecycle

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.leaderboard_frag, container, false);
        return root;
    }

    //endregion

    //region Contract

    @Override
    public void setPresenter(LeaderboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLeaders(List<Person> people) {

    }

    @Override
    public void showAddRound() {
        Snackbar.make(this.getView(), "Presenter start", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingLeaderboardError() {

    }

    @Override
    public void showNoLeaders() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    //endregion
}
