package uk.org.amey.android.coffeeround.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.data.model.TokenResponse;

public class AuthPresenter implements AuthContract.Presenter {

    private final AuthContract.View view;

    public AuthPresenter(@NonNull AuthContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    //region Contract methods

    @Override
    public void start() {
        // Silent sign in if we can...

        // ...otherwise prompt to auth
        view.showAuthPromptView();
    }

    @Override
    public void signInButtonClicked() {
        view.signIn();
    }

    public void authResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

//            CoffeeRoundClientGenerator.idToken = account.getIdToken();
//            view.showAuthenticatedView();

            Call<TokenResponse> call = CoffeeRoundClientGenerator.getClient().login(account.getIdToken());
            call.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    if (response.isSuccessful()) {
                        String jwt = response.body().token;
                        if (jwt != null && jwt.length() > 0) {
                            CoffeeRoundClientGenerator.setJWT(jwt);
                            view.showAuthenticatedView();
                            return;
                        }
                    }

                    // Something was wrong if we're here
                    Log.d("AuthPresenter", "Error retrieving token");
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Log.d("AuthPresenter", "Login call failed");
                }
            });

//            Call<Void> call = CoffeeRoundClientGenerator.getClient(idToken).signIn(idToken);
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        view.showAuthenticatedView();
//                    } else {
//                        Log.d("bad", "bad1");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Log.d("bad", "bad2");
//                }
//            });
        }
    }

    public void connectionFailed(ConnectionResult connectionResult) {

    }

    //endregion
}
