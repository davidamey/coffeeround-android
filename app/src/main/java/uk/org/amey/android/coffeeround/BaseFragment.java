package uk.org.amey.android.coffeeround;

import android.app.Fragment;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment implements BasePresenter.ViewInterface {

    @Override
    public void showError(String msg) {
        View v = getView();
        if (v != null) {
            Snackbar sb = Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(Color.RED);
            sb.show();
        } else {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

}
