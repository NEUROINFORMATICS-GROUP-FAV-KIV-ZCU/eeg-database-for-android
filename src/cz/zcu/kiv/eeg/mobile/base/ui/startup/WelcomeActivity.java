package cz.zcu.kiv.eeg.mobile.base.ui.startup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.TestCredentials;
import org.holoeverywhere.widget.EditText;

/**
 * Activity for user's log in process.
 *
 * @author Petr Miko
 */
public class WelcomeActivity extends CommonActivity {

    private final static String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Welcome activity displayed");
        setContentView(R.layout.base_welcome);

        EditText urlField = (EditText) findViewById(R.id.settings_url_field);

        urlField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    loginClick();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogin:
                loginClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles login button click event. Grabs credentials and endpoint from fields and tests credentials.
     */
    public void loginClick() {

        if (!ConnectionUtils.isOnline(this)) {
            showAlert(getString(R.string.error_offline));
            return;
        }

        EditText usernameField = (EditText) findViewById(R.id.settings_username_field);
        EditText passwordField = (EditText) findViewById(R.id.settings_password_field);
        EditText urlField = (EditText) findViewById(R.id.settings_url_field);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String url = urlField.getText().toString();


        SharedPreferences credentials = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = credentials.edit();

        boolean error;

        if (error = ValidationUtils.isUsernameFormatInvalid(username))
            usernameField.setError(getString(R.string.error_invalid_username));
        if (error |= ValidationUtils.isPasswordFormatInvalid(password))
            passwordField.setError(getString(R.string.error_invalid_password));
        if (error |= ValidationUtils.isUrlFormatInvalid(url))
            urlField.setError(getString(R.string.error_invalid_url));


        if (!error) {

            editor.putString("tmp_username", username);
            editor.putString("tmp_password", password);
            editor.putString("tmp_url", url + Values.ENDPOINT);

            editor.commit();

            new TestCredentials(this, true).execute();
        }
    }
}
