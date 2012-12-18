package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;

public class DetailsActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction;
        DetailsFragment details;

		if (savedInstanceState == null) {
			details = new DetailsFragment();
            details.setArguments(getIntent().getExtras());
            fragmentManager.beginTransaction();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(android.R.id.content, details, ReservationListFragment.TAG);
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