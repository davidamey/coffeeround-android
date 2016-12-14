package uk.org.amey.android.coffeeround.newround;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.data.CoffeeRoundApi;
import uk.org.amey.android.coffeeround.data.model.User;

public class NewRoundPresenter extends BasePresenter<NewRoundPresenter.ViewInterface> {

    interface ViewInterface extends BasePresenter.ViewInterface {
        Observable<Void> onSubmitNewRound(); // Needs submitted data

        void showLoading();
        void hideLoading();
        void showError(String msg);

        void showUsers(List<User> users);
    }

    private final List<User> users = new ArrayList<User>();

    public NewRoundPresenter(CoffeeRoundApi client) {

        for (int i = 0; i < 100; i++) {
            final String id = String.valueOf(i);
            users.add(new User() {
                {
                    name = "User " + id;
                    picture = "http://picture" + id;
                }
            });
        }
    }

    @Override
    public void register(ViewInterface view) {
        super.register(view);

        view.showUsers(users);
    }
}
