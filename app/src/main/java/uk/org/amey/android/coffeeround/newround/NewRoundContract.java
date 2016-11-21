package uk.org.amey.android.coffeeround.newround;

import java.util.List;

import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.BaseView;
import uk.org.amey.android.coffeeround.data.model.User;

public interface NewRoundContract {

    interface View extends BaseView<NewRoundContract.Presenter> {

        void setLoadingIndicator(boolean active);

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

    }

}
