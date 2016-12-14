package uk.org.amey.android.coffeeround.newround;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.User;


public class NewRoundFragment extends Fragment implements NewRoundPresenter.ViewInterface {

    private final NewRoundPresenter presenter = new NewRoundPresenter(CoffeeRoundClientGenerator.getClient());

    private NewRoundAdapter selectedUsersAdapter = new NewRoundAdapter(new ArrayList<User>(0), R.layout.newround_item_selected);
    private NewRoundAdapter allUsersAdapter = new NewRoundAdapter(new ArrayList<User>(0), R.layout.newround_item);

    private RecyclerView selectedUsersRV;
    private RecyclerView allUsersRV;

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
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(R.layout.newround_frag, container, false);

        LinearLayoutManager selectedUsersLM = new LinearLayoutManager(this.getActivity());
        selectedUsersLM.setOrientation(LinearLayoutManager.HORIZONTAL);
        selectedUsersRV = (RecyclerView) root.findViewById(R.id.users_selected);
        selectedUsersRV.setLayoutManager(selectedUsersLM);
        selectedUsersRV.setAdapter(selectedUsersAdapter);
        selectedUsersAdapter.onItemClicked()
                .subscribe(data -> {
                    selectedUsersAdapter.remove(data.position);
                    allUsersAdapter.deselect(data.user);
                });

        GridLayoutManager allUsersLM = new GridLayoutManager(getActivity(), 2);
        allUsersRV = (RecyclerView) root.findViewById(R.id.users_all);
        allUsersRV.setLayoutManager(allUsersLM);
//        allUsersRV.addItemDecoration(new DividerItemDecoration(leaderBoard.getContext(), selectedUsersLM.getOrientation()));
        allUsersRV.setAdapter(allUsersAdapter);
        allUsersAdapter.onItemClicked()
                .subscribe(data -> {
                    if (data.selected) {
                        selectedUsersAdapter.add(data.user);
                    } else {
                        selectedUsersAdapter.remove(data.user);
                    }
                });

        return root;
    }

    //endregion

    //region Contract

    @Override
    public Observable<Void> onSubmitNewRound() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        Snackbar sb = Snackbar.make(this.getView(), msg, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(Color.RED);
        sb.show();
    }

    @Override
    public void showUsers(List<User> users) {
        allUsersAdapter.replaceData(users);
    }

    //endregion
}
