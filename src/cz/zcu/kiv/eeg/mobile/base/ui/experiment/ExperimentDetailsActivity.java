package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
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

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_info);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction;
        ExperimentDetailsFragment details;

        if (savedInstanceState == null) {
            details = new ExperimentDetailsFragment();
            details.setArguments(getIntent().getExtras());
            fragmentManager.beginTransaction();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(android.R.id.content, details);
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