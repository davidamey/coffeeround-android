package uk.org.amey.android.coffeeround.auth;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.org.amey.android.coffeeround.BasePresenterRx;
import uk.org.amey.android.coffeeround.data.CoffeeRoundApi;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.TokenResponse;

class AuthPresenter extends BasePresenterRx<AuthPresenter.ViewRx> {

    interface ViewRx extends BasePresenterRx.ViewInterface {
        Observable<GoogleSignInResult> onSignInResult();

        void showLoading();
        void hideLoading();
        void showError(String msg);

        void showAuthPromptView();
        void showAuthenticatedView();
    }

    private final CoffeeRoundApi client;

    public AuthPresenter(CoffeeRoundApi client) {
        this.client = client;
    }

    @Override
    public void register(ViewRx view) {
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
                    Observable<TokenResponse> response = client.login(idToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorResumeNext(throwable -> {
                                // TODO: handle error
                                return Observable.just(null);
                            });

                    return response;
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
