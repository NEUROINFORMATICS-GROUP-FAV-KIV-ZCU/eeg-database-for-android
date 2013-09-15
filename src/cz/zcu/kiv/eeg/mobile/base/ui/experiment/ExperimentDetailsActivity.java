package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;

/**
 * Activity for displaying experiments details.
 * Only contains ExperimentDetailsFragments.
 * Used on devices < 7", where details cannot fit next to experiment list.
 *
 * @author Petr Miko
 */
public class ExperimentDetailsActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_frame);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_info);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        ExperimentDetailsFragment details;

        if (savedInstanceState == null) {
            details = new ExperimentDetailsFragment();
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}