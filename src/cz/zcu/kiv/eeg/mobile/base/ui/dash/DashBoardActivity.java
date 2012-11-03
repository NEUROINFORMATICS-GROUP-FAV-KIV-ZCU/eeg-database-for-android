package cz.zcu.kiv.eeg.mobile.base.ui.dash;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import android.os.Bundle;
import android.util.Log;

public class DashBoardActivity extends CommonActivity {
	
	private static final String TAG = DashBoardActivity.class.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "App started");
		setTitle(R.string.app_dashboard);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

}
