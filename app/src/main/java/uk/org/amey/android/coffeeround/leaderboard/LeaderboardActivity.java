package uk.org.amey.android.coffeeround.leaderboard;

<<<<<<< HEAD
=======
import android.app.FragmentTransaction;
>>>>>>> fd6c2ed50362228080f188ff735962147363fd60
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.org.amey.android.coffeeround.R;

<<<<<<< HEAD
public class LeaderboardActivity extends AppCompatActivity {
=======
public class LeaderboardActivity extends AppCompatActivity{
>>>>>>> fd6c2ed50362228080f188ff735962147363fd60
    private LeaderboardContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.leaderboard_act);

        LeaderboardFragment leaderboardFragment =
                (LeaderboardFragment) getFragmentManager().findFragmentById(R.id.content_view);

        if (leaderboardFragment == null) {
            leaderboardFragment = LeaderboardFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.content_view, leaderboardFragment)
                    .commit();
        }

        presenter = new LeaderboardPresenter(leaderboardFragment);
    }
}
