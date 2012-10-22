package cz.zcu.kiv.eeg.mobile.base.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
		startActivity(welcomeIntent);
		finish();
	}

}
