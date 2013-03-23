package cz.zcu.kiv.eeg.mobile.base.ui.startup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.ui.NavigationActivity;

/**
 * Activity started on application startup.
 * Tests whether are available user credentials.
 * If so, NavigationActivity is displayed.
 * Otherwise, WelcomeActivity is displayed so user could log in.
 *
 * @author Petr Miko
 */
public class StartUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences credentials = getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
        CharSequence username = credentials.getString("username", null);
        CharSequence password = credentials.getString("password", null);

        if (username == null || password == null) {
            Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
            startActivity(welcomeIntent);
        } else {
            Intent mainIntent = new Intent(this, NavigationActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
        }

        finish();
    }

}
