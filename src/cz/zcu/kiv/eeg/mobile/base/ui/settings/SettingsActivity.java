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
package cz.zcu.kiv.eeg.mobile.base.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.TestCredentials;

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

        SharedPreferences credentials = getCredentials();
        CharSequence username = credentials.getString("username", null);
        CharSequence password = credentials.getString("password", null);
        CharSequence url = credentials.getString("url", "https://");
        TextView usernameField = (TextView) findViewById(R.id.settings_username_field);
        TextView passwordField = (TextView) findViewById(R.id.settings_password_field);
        TextView urlField = (TextView) findViewById(R.id.settings_url_field);
        usernameField.setText(username);
        passwordField.setText(password);
        urlField.setText(url);

        CompoundButton checkBox = (CompoundButton) findViewById(R.id.settings_monday_first_day);
        checkBox.setChecked(getVarious().getBoolean("monday", false));
    }

    /**
     * {@inheritDoc}
     */
    public void save() {

        TextView usernameField = (TextView) findViewById(R.id.settings_username_field);
        TextView passwordField = (TextView) findViewById(R.id.settings_password_field);
        TextView urlField = (TextView) findViewById(R.id.settings_url_field);

        testCredentials(usernameField.getText().toString(), passwordField.getText().toString(), urlField.getText().toString());

        CompoundButton checkBox = (CompoundButton) findViewById(R.id.settings_monday_first_day);

        SharedPreferences.Editor editor = getVarious().edit();
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

        SharedPreferences credentials = getCredentials();
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
