package uk.org.amey.android.coffeeround.auth;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.org.amey.android.coffeeround.R;

public class AuthFragment extends Fragment {

    private AuthContract.Presenter presenter;

    public AuthFragment() {
        // Requires empty public constructor
    }

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    //region Lifecycle

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.auth_frag, container, false);

        root.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return root;
    }

    //endregion

    //region Contract

//    @Override
    public void showAuthPromptView() {

    }

//    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        this.presenter = presenter;
    }

//    @Override
    public boolean isActive() {
        return isAdded();
    }

    //endregion
}
