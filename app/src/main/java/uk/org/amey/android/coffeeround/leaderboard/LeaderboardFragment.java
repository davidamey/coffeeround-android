package uk.org.amey.android.coffeeround.leaderboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.model.User;

public class LeaderboardFragment extends Fragment implements LeaderboardPresenter.ViewInterface {

    private final LeaderboardPresenter presenter = new LeaderboardPresenter();

    private LeaderboardAdapter leaderboardAdapter;
    private RecyclerView leaderBoard;
    private android.view.View loadingView;

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
        presenter.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unregister();
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(R.layout.leaderboard_frag, container, false);

        LinearLayoutManager lm = new LinearLayoutManager(this.getActivity());

        leaderBoard = (RecyclerView) root.findViewById(R.id.leaderboard_list);
        leaderBoard.setLayoutManager(lm);
        leaderBoard.addItemDecoration(new DividerItemDecoration(leaderBoard.getContext(), lm.getOrientation()));
        leaderBoard.setAdapter(leaderboardAdapter);

        loadingView = root.findViewById(R.id.leaderboard_loader);

        return root;
    }

    //endregion

    //region Contract

    @Override
    public Observable<Void> onRefreshAction() {
        return Observable.just(null);
    }


    @Override
    public void showLoading() {
        loadingView.setVisibility(android.view.View.VISIBLE);
        leaderBoard.setVisibility(android.view.View.GONE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(android.view.View.GONE);
        leaderBoard.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void showLeaderboard(List<User> users) {
        if (users == null || users.size() == 0) {
            // Show no users view
        } else {
            leaderboardAdapter.replaceData(users);
        }
    }

    @Override
    public void showLoadingLeaderboardError() {
        Snackbar.make(this.getView(), "Oh noes!", Snackbar.LENGTH_SHORT).show();
    }

    //endregion

    //region Adapter

    private static class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
        private List<User> users;
        private LeaderboardItemListener itemListener;

        static class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvUserName;
            private final TextView tvCountTotal;

            ViewHolder(android.view.View itemView) {
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
            android.view.View v = LayoutInflater.from(parent.getContext())
                                   .inflate(R.layout.leaderboard_item, parent, false);

            return new ViewHolder(v);
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

        void replaceData(List<User> users) {
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
