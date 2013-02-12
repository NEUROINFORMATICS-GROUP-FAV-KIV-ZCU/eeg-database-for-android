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
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;

public class CommonActivity extends Activity {

    protected volatile ProgressDialog progressDialog;
    public static CommonService service;
    private String progressMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        synchronized (CommonActivity.class) {
            if (service != null) {
                service.setActivity(this);

                if (savedInstanceState != null) {
                    progressMessage = savedInstanceState.getString("progressMsg");
                    progressDialog = ProgressDialog.show(CommonActivity.this,
                            getString(R.string.working), progressMessage, true, false);

                }
            }
        }
    }

    public void changeProgress(ServiceState state) {
        changeProgress(state, null);
    }

    public void changeProgress(final ServiceState state, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case RUNNING:
                        progressMessage = message;
                        progressDialog = ProgressDialog.show(CommonActivity.this,
                                getString(R.string.working), message, true, false);
                        break;
                    case INACTIVE:
                    case DONE:
                        synchronized (CommonActivity.class) {
                            service = null;
                            progressMessage = null;
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
            }
        });
    }

    public void showAlert(String alert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alert).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (service != null && progressDialog != null) {
            outState.putString("progressMsg", progressMessage);
        }
    }

    @Override
    protected void onPause() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPause();
    }

    protected SharedPreferences getCredentials() {
        return getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
    }

    protected SharedPreferences getVarious() {
        return getSharedPreferences(Values.PREFS_VARIOUS, Context.MODE_PRIVATE);
    }
}
