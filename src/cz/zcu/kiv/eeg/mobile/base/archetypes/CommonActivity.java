package cz.zcu.kiv.eeg.mobile.base.archetypes;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import cz.zcu.kiv.eeg.mobile.base.R;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.ProgressDialog;

/**
 * Activity with capability of recognizing CommonService state.
 * If CommonService was set and activity is recreated, progress dialog is recreated as well.
 *
 * @author Petr Miko
 */
public abstract class CommonActivity extends Activity {

    private static final String TAG = CommonActivity.class.getSimpleName();

    /**
     * Progress dialog informing of common service state.
     */
    private static ProgressDialog progressDialog;


    /**
     * Actions performed upon activity creation.
     *
     * @param savedInstanceState information bundle from previously destroyed instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        synchronized (CommonActivity.class) {
            ServiceReference.refreshReferences(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgressDialog();
    }

    final void onServiceStart(int message) {
        ServiceReference.start(message);
        updateProgressDialog();
    }

    final void onServiceDone(int message) {
        ServiceReference.done(message);
        updateProgressDialog();
    }

    private void updateProgressDialog() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ServiceReference current = ServiceReference.peek();

                if (current == null && progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                } else if (current != null) {

                    if (progressDialog == null) {
                        progressDialog = ProgressDialog.show(CommonActivity.this,
                                getString(R.string.working), getString(current.message), true, false);
                    } else {
                        progressDialog.setMessage(getString(current.message));
                    }
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
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CommonActivity.this);
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
        });
    }

    /**
     * Getter of various information bundle (various as without obvious category).
     *
     * @return various information bundle
     */
    protected SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Informs activity whether there are running CommonServices.
     *
     * @return true if working CommonService
     */
    protected boolean isWorking() {
        return ServiceReference.peek() != null;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void onServiceError(int message, String errorMessage) {
        showAlert("ERROR: " + getString(message) + ":\n" + errorMessage);
        onServiceDone(message);
    }
}