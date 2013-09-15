package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;

/**
 * Activity for displaying reservation details.
 * On create activity fills itself with ReservationDetailsFragment.
 *
 * @author Petr Miko
 */
public class ReservationDetailsActivity extends CommonActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_info);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        ReservationDetailsFragment details;

        if (savedInstanceState == null) {
            details = new ReservationDetailsFragment();
            details.setArguments(getIntent().getExtras());
            fragmentManager.beginTransaction();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(android.R.id.content, details, ReservationFragment.TAG);
            transaction.commit();
        }
    }

    /**
     * On up (home) button click closes activity.
     */
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