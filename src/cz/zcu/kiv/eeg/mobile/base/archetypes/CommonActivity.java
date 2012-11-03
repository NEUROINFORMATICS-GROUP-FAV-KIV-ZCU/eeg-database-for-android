package cz.zcu.kiv.eeg.mobile.base.archetypes;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

public class CommonActivity extends FragmentActivity {

	protected volatile ProgressDialog progressDialog;

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
							getString(R.string.working), message, true, true);
					break;
				case INACTIVE:
				case DONE:
					if (progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
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

}
