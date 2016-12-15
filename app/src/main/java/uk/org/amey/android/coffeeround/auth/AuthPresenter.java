package uk.org.amey.android.coffeeround.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.data.CoffeeRoundApi;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.TokenResponse;

class AuthPresenter extends BasePresenter<AuthPresenter.ViewInterface> {

    interface ViewInterface extends BasePresenter.ViewInterface {
        Observable<Void> onSignInRequested();
        Observable<GoogleSignInResult> onSignInResult();

        void showLoading();
        void hideLoading();

        void silentSignIn();
        void signIn();

        void showAuthPromptView();
        void showAuthenticatedView();
    }

    private final CoffeeRoundApi client;

    public AuthPresenter(CoffeeRoundApi client) {
        this.client = client;
    }

    @Override
    public void register(ViewInterface view) {
        super.register(view);

        addToUnsubscribe(view.onSignInRequested()
                .subscribe(ignored -> {
                    view.showLoading();
                    view.signIn();
                })
        );

        addToUnsubscribe(view.onSignInResult()
                .switchMap(googleSignInResult -> {
                    if (!googleSignInResult.isSuccess()) {
                        return Observable.just(null);
                    }

                    String idToken = googleSignInResult.getSignInAccount().getIdToken();
                    return client.login(idToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorResumeNext(throwable -> Observable.just(null));
                })
                .subscribe(response -> {
                    if (response == null) {
                        view.hideLoading();
                        view.showError("Unable to sign in");
                        return;
                    }

                    String jwt = response.token;
                    if (jwt != null && jwt.length() > 0) {
                        CoffeeRoundClientGenerator.setJWT(jwt);
                        view.hideLoading();
                        view.showAuthenticatedView();
                    }
                }, throwable -> {
                    view.hideLoading();
                    view.showError("Unable to sign in");
                })
        );

        // Show the auth prompt view but also silent sign in
        view.showAuthPromptView();
        view.showLoading();
        view.silentSignIn();
    }
}
