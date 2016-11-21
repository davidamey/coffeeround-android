package uk.org.amey.android.coffeeround.leaderboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.Person;

public class LeaderboardFragment extends Fragment implements LeaderboardContract.View {

    private LeaderboardContract.Presenter presenter;
    private LeaderboardAdapter leaderboardAdapter;

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

        ArrayList<Person> dummyData = new ArrayList<Person>();
        for (int i = 0; i < 20; i++) {
            dummyData.add(new Person());
        }

        leaderboardAdapter = new LeaderboardAdapter(dummyData, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.leaderboard_frag, container, false);

        RecyclerView rv = (RecyclerView) root.findViewById(R.id.leaderboard_list);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setAdapter(leaderboardAdapter);

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

    //region Adapter
    private static class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
        private List<Person> people;
        private LeaderboardItemListener itemListener;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        public LeaderboardAdapter(List<Person> people, LeaderboardItemListener itemListener) {
            this.people = people;
            this.itemListener = itemListener;
        }

        @Override
        public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                                   .inflate(R.layout.leaderboard_item, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Set the viewholder data.
        }

        @Override
        public int getItemCount() {
            return people.size();
        }
    }

    public interface LeaderboardItemListener {
        void onItemClick(Person clickedPerson);
    }
    //endregion
}
