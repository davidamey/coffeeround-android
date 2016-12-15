package uk.org.amey.android.coffeeround.newround;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.org.amey.android.coffeeround.R;
import uk.org.amey.android.coffeeround.leaderboard.LeaderboardActivity;
import uk.org.amey.android.coffeeround.leaderboard.LeaderboardFragment;

public class NewRoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.newround_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        NewRoundFragment fragment =
                (NewRoundFragment) getFragmentManager().findFragmentById(R.id.content_view);

        if (fragment == null) {
            fragment = NewRoundFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.content_view, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, LeaderboardActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
