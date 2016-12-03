package uk.org.amey.android.coffeeround.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;

import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.BaseView;

public interface AuthContract {

    interface View extends BaseView<AuthContract.Presenter> {

        void showAuthPromptView();

        void signIn();

        void showAuthenticatedView();

    }

    interface Presenter extends BasePresenter {

        void signInButtonClicked();

        void authResult(GoogleSignInResult result);

        void connectionFailed(ConnectionResult connectionResult);

    }

}
