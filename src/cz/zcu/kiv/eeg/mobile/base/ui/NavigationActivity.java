package cz.zcu.kiv.eeg.mobile.base.ui;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.dash.ExperimentFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.AgendaListFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.settings.SettingsActivity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class NavigationActivity extends CommonActivity implements ActionBar.OnNavigationListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sections_list, android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(spinnerAdapter, this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment previousFragment = getFragmentManager().findFragmentById(R.id.details);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		switch (itemPosition) {
		case 0:
			ExperimentFragment expFrag = (previousFragment != null && previousFragment instanceof ExperimentFragment) ? (ExperimentFragment) previousFragment : new ExperimentFragment();
			fragmentTransaction.replace(R.id.content, expFrag);
			fragmentTransaction.commit();
			break;
		case 1:
			AgendaListFragment agendaFrag = (previousFragment != null && previousFragment instanceof AgendaListFragment) ? (AgendaListFragment) previousFragment : new AgendaListFragment();
			fragmentTransaction.replace(R.id.content, agendaFrag);
			fragmentTransaction.commit();
			break;
		case 2:
			Intent intent = new Intent();
			intent.setClass(this, SettingsActivity.class);
			startActivity(intent);
			return false;
		default:
			return false;
		}

		return true;
	}

}
