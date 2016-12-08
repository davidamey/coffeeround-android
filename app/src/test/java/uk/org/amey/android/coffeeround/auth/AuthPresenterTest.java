package uk.org.amey.android.coffeeround.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import uk.org.amey.android.coffeeround.data.CoffeeRoundApi;
import uk.org.amey.android.coffeeround.data.model.TokenResponse;
import uk.org.amey.android.coffeeround.util.RxSchedulersOverrideRule.RxSchedulersOverrideRule;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthPresenterTest {

    @Mock CoffeeRoundApi mockClient;
    @Mock GoogleSignInResult mockSignInResult;
    @Mock GoogleSignInAccount mockSignInAccount;

    @Mock AuthPresenter.ViewRx mockView;

    @Rule
    public final RxSchedulersOverrideRule schedulersOverrideRule = new RxSchedulersOverrideRule();

    @Test
    public void CanSignIn() throws Exception {
        // Setup
        when(mockSignInAccount.getIdToken()).thenReturn("ID_TOKEN");
        when(mockSignInResult.isSuccess()).thenReturn(true);
        when(mockSignInResult.getSignInAccount()).thenReturn(mockSignInAccount);
        when(mockView.onSignInResult()).thenReturn(Observable.just(mockSignInResult));
        when(mockClient.login("ID_TOKEN")).thenReturn(Observable.just(new TokenResponse() { { token = "JWT_TOKEN"; } } ));

        // Act
        AuthPresenter presenter = new AuthPresenter(mockClient);
        presenter.register(mockView);

        // Assert
        verify(mockView, times(1)).onSignInResult();
        verify(mockView, times(1)).showLoading();
        verify(mockClient, times(1)).login("ID_TOKEN");
        verify(mockView, times(1)).hideLoading();
        verify(mockView, times(1)).showAuthenticatedView();
    }

}
