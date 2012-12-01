package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class DetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			DetailsFragment details = new DetailsFragment();
			details.setArguments(getIntent().getExtras());
			getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
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