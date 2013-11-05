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
package cz.zcu.kiv.eeg.mobile.base.archetypes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;
import cz.zcu.kiv.eeg.mobile.base.data.Values;

/**
 * Activity with capability of recognizing CommonService state.
 * If CommonService was set and activity is recreated, progress dialog is recreated as well.
 *
 * @author Petr Miko
 */
public class CommonActivity extends Activity {

    private static final String TAG = CommonActivity.class.getSimpleName();
    /**
     * Progress dialog informing of common service state.
     */
    protected volatile ProgressDialog progressDialog;

    /**
     * Actions performed upon activity creation.
     *
     * @param savedInstanceState information bundle from previously destroyed instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        synchronized (CommonActivity.class) {
            if (!ServiceReference.isEmpty()) {
                ServiceReference.refreshReferences(this);
                progressDialog = ProgressDialog.show(CommonActivity.this,
                        getString(R.string.working), ServiceReference.peek().message, true, false);

            }
        }
    }

    /**
     * Sets progress dialog.
     *
     * @param state new state of progress dialog
     */
    public void changeProgress(ServiceState state) {
        changeProgress(state, null);
    }

    /**
     * Sets progress dialog and its message.
     *
     * @param state   new state of progress dialog
     * @param message message to be displayed
     */
    public void changeProgress(final ServiceState state, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                try {
                    switch (state) {
                        case RUNNING:
                            ServiceReference current = ServiceReference.peek();
                            current.message = message;
                            progressDialog = ProgressDialog.show(CommonActivity.this,
                                    getString(R.string.working), message, true, false);
                            break;
                        case INACTIVE:
                        case DONE:
                            synchronized (CommonActivity.class) {
                                ServiceReference.pop();
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                            break;
                        case ERROR:
                            showAlert(message);
                        default:
                            break;
                    }
                } catch (Exception e) {
                    Log.w(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * Display alert dialog.
     *
     * @param alert message
     */
    public void showAlert(final String alert) {
        showAlert(alert, false);
    }

    /**
     * Method for showing alert dialog with option, whether activity should be finished after confirmation.
     *
     * @param alert         alert message
     * @param closeActivity close activity on confirmation
     */
    public void showAlert(final String alert, final boolean closeActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alert).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (closeActivity) {
                            finish();
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * Actions performed when activity stops being visible on screen.
     */
    @Override
    protected void onPause() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPause();
    }

    /**
     * Getter of credentials information bundle.
     *
     * @return credentials preferences
     */
    protected SharedPreferences getCredentials() {
        return getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
    }

    /**
     * Getter of various information bundle (various as without obvious category).
     *
     * @return various information bundle
     */
    protected SharedPreferences getVarious() {
        return getSharedPreferences(Values.PREFS_VARIOUS, Context.MODE_PRIVATE);
    }

    /**
     * Informs activity whether there are running CommonServices.
     *
     * @return true if working CommonService
     */
    protected boolean isWorking() {
        return !ServiceReference.isEmpty();
    }
}
