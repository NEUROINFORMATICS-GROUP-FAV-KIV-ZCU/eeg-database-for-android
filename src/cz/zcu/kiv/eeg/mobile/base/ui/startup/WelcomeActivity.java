package cz.zcu.kiv.eeg.mobile.base.ui.startup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Constants;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.TestCredentials;

public class WelcomeActivity extends CommonActivity {

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

		TextView usernameField = (TextView) findViewById(R.id.settings_username_field);
		TextView passwordField = (TextView) findViewById(R.id.settings_password_field);
		TextView urlField = (TextView) findViewById(R.id.settings_url_field);

		testCredentials(usernameField.getText().toString(), passwordField.getText().toString(), urlField.getText()
				.toString());
	}

	private void testCredentials(String username, String password, String url) {

		if (!ConnectionUtils.isOnline(this)) {
			showAlert(getString(R.string.error_offline));
			return;
		}

		SharedPreferences credentials = getSharedPreferences(Constants.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = credentials.edit();

		StringBuilder error = new StringBuilder();

		if (ValidationUtils.isUsernameFormatInvalid(username))
			error.append(getString(R.string.error_invalid_username)).append('\n');
		if (ValidationUtils.isPasswordFormatInvalid(password))
			error.append(getString(R.string.error_invalid_password)).append('\n');
		if (ValidationUtils.isUrlFormatInvalid(url))
			error.append(getString(R.string.error_invalid_url)).append('\n');

		if (error.toString().isEmpty()) {

			if (url != null && !url.endsWith("/"))
				url += "/";

			editor.putString("tmp_username", username.toString());
			editor.putString("tmp_password", password.toString());
			editor.putString("tmp_url", url);
			editor.commit();

			new TestCredentials(this, true).execute();
		} else {
			showAlert(error.toString());
		}
	}
}
