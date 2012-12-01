package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.os.*;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.NavigationActivity;

/**
 * 
 * @author Petr Miko
 * 
 */
public class AgendaActivity extends NavigationActivity {

	private static final String TAG = AgendaActivity.class.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "App started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reser_agenda);
	}
}