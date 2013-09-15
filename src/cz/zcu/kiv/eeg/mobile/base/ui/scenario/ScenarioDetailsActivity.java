package cz.zcu.kiv.eeg.mobile.base.ui.scenario;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;

/**
 * Activity for displaying scenario details.
 * Only creates activity and inflates ScenarioDetailsFragment inside of itself.
 *
 * @author Petr Miko
 */
public class ScenarioDetailsActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_frame);

        //enables up button, sets section icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_info);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        ScenarioDetailsFragment details;

        if (savedInstanceState == null) {
            details = new ScenarioDetailsFragment();
            details.setArguments(getIntent().getExtras());
            fragmentManager.beginTransaction();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, details);
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}