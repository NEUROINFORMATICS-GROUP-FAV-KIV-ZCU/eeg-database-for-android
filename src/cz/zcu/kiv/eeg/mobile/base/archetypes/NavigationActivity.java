package cz.zcu.kiv.eeg.mobile.base.archetypes;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.ui.dash.DashBoardActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.AgendaActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.settings.SettingsActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class NavigationActivity extends CommonActivity implements ActionBar.OnNavigationListener {

	private boolean isCreated = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sections_list, android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(spinnerAdapter, this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		if (!isCreated)
			try {
				switch (itemPosition) {
				case 0:
					if (!(this instanceof DashBoardActivity)) {
						Intent intent = new Intent();
						intent.setClass(this, DashBoardActivity.class);
						startActivity(intent);
					}

					break;
				case 1:
					if (!(this instanceof AgendaActivity)) {
						Intent intent = new Intent();
						intent.setClass(this, AgendaActivity.class);
						startActivity(intent);
					}
					break;
				case 2:
					if (!(this instanceof SettingsActivity)) {
						Intent intent = new Intent();
						intent.setClass(this, SettingsActivity.class);
						startActivity(intent);

					}
					break;
				default:
					return false;
				}
			} finally {
				finish();
			}

		isCreated = false;
		return true;
	}

}
