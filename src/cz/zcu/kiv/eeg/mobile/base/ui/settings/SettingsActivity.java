package cz.zcu.kiv.eeg.mobile.base.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.TestCredentials;
import org.holoeverywhere.widget.EditText;

import java.util.Calendar;

/**
 * Activity for setting user's preferences.
 * Mainly contains credentials for logging into eeg base.
 *
 * @author Petr Miko
 */
public class SettingsActivity extends SaveDiscardActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Settings screen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences credentials = getPreferences();
        CharSequence username = credentials.getString("username", null);
        CharSequence password = credentials.getString("password", null);
        CharSequence url = credentials.getString("url", "https://");
        EditText usernameField = (EditText) findViewById(R.id.settings_username_field);
        EditText passwordField = (EditText) findViewById(R.id.settings_password_field);
        EditText urlField = (EditText) findViewById(R.id.settings_url_field);
        usernameField.setText(username);
        passwordField.setText(password);
        urlField.setText(url);

        CompoundButton checkBox = (CompoundButton) findViewById(R.id.settings_monday_first_day);
        checkBox.setChecked(getPreferences().getBoolean("monday", false));
    }

    /**
     * {@inheritDoc}
     */
    public void save() {

        EditText usernameField = (EditText) findViewById(R.id.settings_username_field);
        EditText passwordField = (EditText) findViewById(R.id.settings_password_field);
        EditText urlField = (EditText) findViewById(R.id.settings_url_field);

        testCredentials(usernameField.getText().toString(), passwordField.getText().toString(), urlField.getText().toString());

        CompoundButton checkBox = (CompoundButton) findViewById(R.id.settings_monday_first_day);

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean("monday", checkBox.isChecked());
        Values.firstDayOfWeek = checkBox.isChecked() ? Calendar.MONDAY : Calendar.SUNDAY;
        editor.commit();
    }

    /**
     * {@inheritDoc}
     */
    protected void discard() {
        finish();
    }

    /**
     * If online, validates whether all required fields are filled correctly.
     * If so, starts TestCredentials service.
     *
     * @param username credentials username
     * @param password credentials password
     * @param url      eeg base rest endpoint
     */
    private void testCredentials(String username, String password, String url) {

        if (!ConnectionUtils.isOnline(this)) {
            showAlert(getString(R.string.error_offline));
            return;
        }

        SharedPreferences credentials = getPreferences();
        SharedPreferences.Editor editor = credentials.edit();

        StringBuilder error = new StringBuilder();

        if (ValidationUtils.isUsernameFormatInvalid(username))
            error.append(getString(R.string.error_invalid_username)).append('\n');
        if (ValidationUtils.isPasswordFormatInvalid(password))
            error.append(getString(R.string.error_invalid_password)).append('\n');
        if (ValidationUtils.isUrlFormatInvalid(url))
            error.append(getString(R.string.error_invalid_url)).append('\n');

        if (error.toString().isEmpty()) {

            editor.putString("tmp_username", username);
            editor.putString("tmp_password", password);
            editor.putString("tmp_url", url);
            editor.commit();

            new TestCredentials(this, false).execute();
        } else {
            showAlert(error.toString());
        }
    }
}
