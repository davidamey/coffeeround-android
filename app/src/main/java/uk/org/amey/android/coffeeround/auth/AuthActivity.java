package uk.org.amey.android.coffeeround.auth;

import android.app.ProgressDialog;
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
import com.google.android.gms.common.api.OptionalPendingResult;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;

import rx.Observable;
import uk.org.amey.android.coffeeround.BasePresenter;
import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.data.CoffeeRoundClientGenerator;
import uk.org.amey.android.coffeeround.leaderboard.LeaderboardActivity;

public class AuthActivity extends AppCompatActivity implements
        AuthPresenter.ViewInterface,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String SERVER_CLIENT_ID = "935541831045-q8djcuoo3hdrnvg6nvpjjk45lg0gps9o.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 9001;

    private BasePresenter presenter;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private ProgressDialog progressDialog;

    private BehaviorRelay<GoogleSignInResult> signInRelay = BehaviorRelay.create();
    private PublishRelay<Void> signInClickRelay = PublishRelay.create();

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
        RxView.clicks(signInButton).subscribe(signInClickRelay);

        // TODO: Inject this client from somewhere other than the view - probably Dagger
        presenter = new AuthPresenter(CoffeeRoundClientGenerator.getClient());
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
                signInRelay.call(result);
            } else {
                signInRelay.call(null);
            }
        }
    }

    //endregion

    //region Contract

    @Override
    public Observable<Void> onSignInRequested() {
        return signInClickRelay;
    }

    @Override
    public Observable<GoogleSignInResult> onSignInResult() {
        return signInRelay;
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing in...");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void showError(String msg) {
        Snackbar sb = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(Color.RED);
        sb.show();
    }

    @Override
    public void silentSignIn() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if (opr.isDone()) {
            // There's immediate result available.
            signInRelay.call(opr.get());
        } else {
            // There's no immediate result ready, displays some progress indicator and waits for the
            // async callback.
            opr.setResultCallback(signInResult -> {
                signInRelay.call(signInResult);
            });
        }
    }

    @Override
    public void showAuthPromptView() {
        signInButton.setVisibility(View.VISIBLE);
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showAuthenticatedView() {
        Intent toAuthIntent = new Intent(this, LeaderboardActivity.class);
        toAuthIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toAuthIntent);
        finish();
    }

    //endregion

    //region GoogleApi

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        signInRelay.call(null);
    }

    //endregion
}
