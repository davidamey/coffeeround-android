package uk.org.amey.android.coffeeround.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.net.ConnectException;

import rx.Observable;
import rx.subjects.AsyncSubject;
import uk.org.amey.android.coffeeround.BasePresenterRx;
import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.leaderboard.LeaderboardActivity;

public class AuthActivity extends AppCompatActivity implements
        AuthPresenter.ViewRx,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String SERVER_CLIENT_ID = "935541831045-q8djcuoo3hdrnvg6nvpjjk45lg0gps9o.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 9001;

    private BasePresenterRx presenter;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;

    // Create an initial subject although this gets recreated in the signIn() method.
    private AsyncSubject<GoogleSignInResult> signInSubject = AsyncSubject.create();

    //region Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SERVER_CLIENT_ID)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setContentView(R.layout.auth_act);

        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(ignored -> signIn());

        presenter = new AuthPresenter();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                signInSubject.onNext(result);
                signInSubject.onCompleted();
            } else {
                signInSubject.onError(new Exception("Unable to sign in"));
            }
        }
    }

    //endregion

    //region Contract

    @Override
    public Observable<GoogleSignInResult> onSignInResult() {
        return signInSubject;
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void showError(String msg) {
        Snackbar sb = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(Color.RED);
        sb.show();
    }

    @Override
    public void showAuthPromptView() {
        signInButton.setVisibility(View.VISIBLE);
    }

    public void signIn() {
        // New signIn attempt so we need a new observable for the presenter to subscribe to.
        // The presenter subscribes (atm) in onResume so will always pick this up after we return
        // from the GoogleSignIn activity. #neverfails
        signInSubject = AsyncSubject.create();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showAuthenticatedView() {
        Intent toAuthIntent = new Intent(this, LeaderboardActivity.class);
        startActivity(toAuthIntent);
    }

    //endregion

    //region GoogleApi

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        signInSubject.onError(new ConnectException(connectionResult.getErrorMessage()));
    }

    //endregion
}
