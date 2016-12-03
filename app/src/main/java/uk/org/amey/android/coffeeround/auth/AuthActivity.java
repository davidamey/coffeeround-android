package uk.org.amey.android.coffeeround.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.leaderboard.LeaderboardActivity;

public class AuthActivity extends AppCompatActivity implements
        AuthContract.View,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String SERVER_CLIENT_ID = "935541831045-q8djcuoo3hdrnvg6nvpjjk45lg0gps9o.apps.googleusercontent.com";
    private static final int RC_SIGN_IN = 9001;

    private boolean isActive;
    private AuthContract.Presenter presenter;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;

    //region Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestServerAuthCode()
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
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.signInButtonClicked();
            }
        });

        new AuthPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.authResult(result);
        }
    }

    //endregion

    //region Contract

    @Override
    public void showAuthPromptView() {
        signInButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        this.presenter = presenter;
        presenter.start();
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void signIn() {
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
        presenter.connectionFailed(connectionResult);
    }

    //endregion

}
