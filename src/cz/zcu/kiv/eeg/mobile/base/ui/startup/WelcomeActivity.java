/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eeg.mobile.base.ui.startup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.ui.NavigationActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.TestCredentials;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences credentials = getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
        String username;
        if(!(username = credentials.getString("tmp_username", "")).equals("")) {
            TextView usernameField = (TextView) findViewById(R.id.settings_username_field);
            TextView passwordField = (TextView) findViewById(R.id.settings_password_field);
            TextView urlField = (TextView) findViewById(R.id.settings_url_field);
            usernameField.setText(username);
            passwordField.setText(credentials.getString("tmp_password", ""));
            urlField.setText(credentials.getString("tmp_url", "https://").replaceAll("(rest)$", ""));
        }
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

    /**
     * Handles login button click event. Grabs credentials and endpoint from fields and tests credentials.
     */
    public void loginClick() {

        TextView usernameField = (TextView) findViewById(R.id.settings_username_field);
        TextView passwordField = (TextView) findViewById(R.id.settings_password_field);
        TextView urlField = (TextView) findViewById(R.id.settings_url_field);

        //Not going for the web service for now
//        testCredentials(usernameField.getText().toString(), passwordField.getText().toString(), urlField.getText().toString());

        //Instead, just open the NavigationActivity and save the credentials
        SharedPreferences credentials = getCredentials();
        SharedPreferences.Editor editor = credentials.edit();
        editor.putString("username", usernameField.getText().toString());
        editor.putString("password", passwordField.getText().toString());
        editor.putString("url", urlField.getText().toString());
        editor.commit();

        Intent navIntent = new Intent(this, NavigationActivity.class);
        startActivity(navIntent);

    }

    /**
     * Method for testing credentials.
     * If online, credentials validated and if no error found, TestCredentials service is invoked.
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

        SharedPreferences credentials = getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
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
            editor.putString("tmp_url", url.replaceAll("(/)$", "") + Values.ENDPOINT);

            editor.commit();

            new TestCredentials(this, true).execute();
        } else {
            showAlert(error.toString());
        }
    }
}
