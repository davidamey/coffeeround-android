package uk.org.amey.android.coffeeround.leaderboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.org.amey.android.coffeeround.R;

public class LeaderboardActivity extends AppCompatActivity {
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
