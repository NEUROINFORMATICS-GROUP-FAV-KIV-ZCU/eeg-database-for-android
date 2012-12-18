package cz.zcu.kiv.eeg.mobile.base.archetypes;

import android.os.Bundle;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.Constants;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

public class CommonActivity extends Activity {

	protected volatile ProgressDialog progressDialog;
    public static CommonService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(service != null)
            service.setActivity(this);
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
					progressDialog = ProgressDialog.show(CommonActivity.this,
							getString(R.string.working), message, true, false);
					break;
				case INACTIVE:
				case DONE:
                    service = null;
					if (progressDialog != null && progressDialog.isShowing()){
						progressDialog.dismiss();
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
	protected void onPause() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		super.onPause();
	}

	protected SharedPreferences getCredentials() {
		return getSharedPreferences(Constants.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
	}
}
