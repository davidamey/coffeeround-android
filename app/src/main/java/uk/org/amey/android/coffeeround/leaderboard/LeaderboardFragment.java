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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.model.User;

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
        leaderboardAdapter = new LeaderboardAdapter(new ArrayList<User>(0), null);
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
    public void showLeaderboard(List<User> users) {
        leaderboardAdapter.replaceData(users);
    }

    @Override
    public void showAddRound() {
        Snackbar.make(this.getView(), "Presenter start", Snackbar.LENGTH_SHORT).show();
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
        private List<User> users;
        private LeaderboardItemListener itemListener;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvUserName;
            private final TextView tvCountTotal;

            public ViewHolder(View itemView) {
                super(itemView);

                tvUserName = (TextView)itemView.findViewById(R.id.user_name);
                tvCountTotal = (TextView)itemView.findViewById(R.id.count_total);
            }

            public void setName(String name) {
                tvUserName.setText(name);
            }

            public void setCountTotal(int total) {
                tvCountTotal.setText(Integer.toString(total));
            }
        }

        public LeaderboardAdapter(List<User> users, LeaderboardItemListener itemListener) {
            this.users = users;
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
            holder.setName(users.get(position).name);
            holder.setCountTotal(users.get(position).total);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public void replaceData(List<User> users) {
            this.users.clear();
            this.users.addAll(users);
            notifyDataSetChanged();
        }
    }

    public interface LeaderboardItemListener {
        void onItemClick(User clickedUser);
    }
    //endregion
}
