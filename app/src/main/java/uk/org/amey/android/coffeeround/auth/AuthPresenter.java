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
        Observable<GoogleSignInResult> onSignInResult();

        void showLoading();
        void hideLoading();

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
        // Silent sign in if we can...
        // TODO: Silent sign in

        addToUnsubscribe(view.onSignInResult()
                .doOnNext(ignored -> view.showLoading())
                .switchMap(googleSignInResult -> {
                    if (!googleSignInResult.isSuccess()) {
                        return Observable.just(null);
                    }

                    String idToken = googleSignInResult.getSignInAccount().getIdToken();
                    return client.login(idToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorResumeNext(throwable -> {
                                // TODO: handle error
                                return Observable.just(null);
                            });
                })
                .subscribe(response -> {
                    if (response == null) {
                        return;
                    }

                    String jwt = response.token;
                    if (jwt != null && jwt.length() > 0) {
                        CoffeeRoundClientGenerator.setJWT(jwt);
                        view.hideLoading();
                        view.showAuthenticatedView();
                    }
                }, throwable -> view.showError("Unable to sign in"))
        );

        // ...otherwise prompt to auth
        view.showAuthPromptView();
    }
}
