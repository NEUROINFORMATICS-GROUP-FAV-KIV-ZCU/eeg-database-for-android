package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;

/**
 * Activity for displaying reservation details.
 * On create activity fills itself with ReservationDetailsFragment.
 *
 * @author Petr Miko
 */
public class ReservationDetailsActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_action_info);

        FragmentManager fragmentManager = getFragmentManager();
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