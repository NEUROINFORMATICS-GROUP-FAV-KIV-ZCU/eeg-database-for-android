package cz.zcu.kiv.eeg.mobile.base.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import cz.zcu.kiv.eeg.mobile.base.R;

public class WelcomeActivity extends Activity {

	private final static String TAG = WelcomeActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Welcome activity displayed");
		setContentView(R.layout.welcome);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menuLogin:
			loginClick();
		}
		return super.onOptionsItemSelected(item);
	}

	public void loginClick() {
	}
}
