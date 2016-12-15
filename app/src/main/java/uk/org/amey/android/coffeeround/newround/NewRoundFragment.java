package uk.org.amey.android.coffeeround.newround;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxrelay.PublishRelay;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import uk.org.amey.android.coffeeround.BaseFragment;
import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.User;
import uk.org.amey.android.coffeeround.leaderboard.LeaderboardActivity;


public class NewRoundFragment extends BaseFragment implements NewRoundPresenter.ViewInterface {

    private final NewRoundPresenter presenter = new NewRoundPresenter(CoffeeRoundClientGenerator.getClient());

    private NewRoundAdapter selectedUsersAdapter = new NewRoundAdapter(new ArrayList<>(0), R.layout.newround_item_selected);
    private NewRoundAdapter allUsersAdapter = new NewRoundAdapter(new ArrayList<>(0), R.layout.newround_item);

    private RecyclerView selectedUsersRV;
    private RecyclerView allUsersRV;

    private PublishRelay<Void> navigateUpRelay = PublishRelay.create();
    private PublishRelay<Void> saveRoundRelay = PublishRelay.create();

    public NewRoundFragment() {
        // Requires empty public constructor
    }

    public static NewRoundFragment newInstance() {
        return new NewRoundFragment();
    }

    //region Lifecycle

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        selectedUsersAdapter.onItemClicked()
                .subscribe(data -> {
                    selectedUsersAdapter.remove(data.position);
                    allUsersAdapter.deselect(data.user);
                    checkSelectedVisibility();
                });

        allUsersAdapter.onItemClicked()
                .subscribe(data -> {
                    if (data.selected) {
                        selectedUsersAdapter.add(data.user);
                        selectedUsersRV.scrollToPosition(selectedUsersAdapter.getItemCount()-1);
                    } else {
                        selectedUsersAdapter.remove(data.user);
                    }
                    checkSelectedVisibility();
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unregister();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_newround, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(getActivity(), LeaderboardActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
            case R.id.save_round:
                saveRoundRelay.call(null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(R.layout.newround_frag, container, false);

        LinearLayoutManager selectedUsersLM = new LinearLayoutManager(this.getActivity());
        selectedUsersLM.setOrientation(LinearLayoutManager.HORIZONTAL);
        selectedUsersRV = (RecyclerView) root.findViewById(R.id.users_selected);
        selectedUsersRV.setLayoutManager(selectedUsersLM);
        selectedUsersRV.setAdapter(selectedUsersAdapter);

        GridLayoutManager allUsersLM = new GridLayoutManager(getActivity(), 2);
        allUsersRV = (RecyclerView) root.findViewById(R.id.users_all);
        allUsersRV.setLayoutManager(allUsersLM);
//        allUsersRV.addItemDecoration(new DividerItemDecoration(leaderBoard.getContext(), selectedUsersLM.getOrientation()));
        allUsersRV.setAdapter(allUsersAdapter);

        checkSelectedVisibility();

        return root;
    }

    //endregion

    //region Contract

    @Override
    public Observable<Void> onNavigateUp() {
        return navigateUpRelay;
    }

    @Override
    public Observable<Void> onSaveRound() {
        return saveRoundRelay;
    }

    @Override
    public void showLoading() {
        // TODO
    }

    @Override
    public void hideLoading() {
        // TODO
    }



    @Override
    public void showUsers(List<User> users) {
        allUsersAdapter.replaceData(users);
    }

    @Override
    public void navigateToLeaderboard() {
        Intent homeIntent = new Intent(getActivity(), LeaderboardActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    //endregion

    private void checkSelectedVisibility() {
        int visibility = selectedUsersAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        selectedUsersRV.setVisibility(visibility);
    }
}
