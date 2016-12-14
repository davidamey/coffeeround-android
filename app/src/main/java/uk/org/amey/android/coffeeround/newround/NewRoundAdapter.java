package uk.org.amey.android.coffeeround.newround;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.List;

import rx.Observable;
import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.model.User;

class NewRoundAdapter extends RecyclerView.Adapter<NewRoundAdapter.ViewHolder> {

    public static class SelectionChangedItem {
        public final int position;
        public final User user;
        public final boolean selected;

        public SelectionChangedItem(int position, User user, boolean selected) {
            this.position = position;
            this.user = user;
            this.selected = selected;
        }
    }

    private PublishRelay<SelectionChangedItem> itemClickRelay = PublishRelay.create();
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();
    private List<User> users;
    private int itemResource;

    public Observable<SelectionChangedItem> onItemClicked() {
        return itemClickRelay;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUserName;
        private final View checkMark;
        private User user;

        ViewHolder(android.view.View itemView) {
            super(itemView);
            tvUserName = (TextView)itemView.findViewById(R.id.user_name);
            checkMark = itemView.findViewById(R.id.check_mark);
        }

        public void setUser(User user) {
            this.user = user;
            tvUserName.setText(user.name);
        }

        public void setSelected(boolean selected) {
            if (checkMark != null) {
                if (selected) {
                    checkMark.setVisibility(View.VISIBLE);
                } else {
                    checkMark.setVisibility(View.GONE);
                }
            }
        }
    }

    public NewRoundAdapter(List<User> users, int itemResource) {
        this.users = users;
        this.itemResource = itemResource;
    }

    @Override
    public NewRoundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemResource, parent, false);
        ViewHolder vh = new ViewHolder(v);

        RxView.clicks(v)
                .takeUntil(RxView.detaches(parent))
                .map(aVoid -> {
                    int position = vh.getAdapterPosition();
                    boolean selected = !selectedPositions.get(position);
                    selectedPositions.put(position, selected);
                    v.setSelected(selected);
                    notifyItemChanged(position);
                    return new SelectionChangedItem(position, vh.user, selected);
                })
                .subscribe(itemClickRelay);

        return vh;
    }

    @Override
    public void onBindViewHolder(NewRoundAdapter.ViewHolder holder, int position) {
        holder.setUser(users.get(position));
        holder.setSelected(selectedPositions.get(position));
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

    void add(User user) {
        users.add(user);
        notifyItemInserted(users.size()-1);
    }

    void remove(int pos) {
        users.remove(pos);
        notifyItemRemoved(pos);
    }

    void remove(User user) {
        int pos = users.indexOf(user);
        if (pos >= 0) {
            remove(pos);
        }
    }

    public void deselect(User user) {
        int pos = users.indexOf(user);
        if (pos >= 0) {
            selectedPositions.put(pos, false);
            notifyItemChanged(pos);
        }
    }
}
