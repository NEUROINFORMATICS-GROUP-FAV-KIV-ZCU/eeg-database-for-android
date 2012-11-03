package cz.zcu.kiv.eeg.mobile.base.archetypes;

import cz.zcu.kiv.eeg.mobile.base.data.Constants;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public abstract class CommonService<T, U, V> extends AsyncTask<T, U, V> {

	protected CommonActivity activity;

	public CommonService(CommonActivity context) {
		this.activity = context;
	}
	
	protected void setState(ServiceState state){
		activity.changeProgress(state);
	}
	
	protected void setState(ServiceState state, int messageCode){
		activity.changeProgress(state, activity.getString(messageCode));
	}
	
	protected void setState(ServiceState state, String message) {
		activity.changeProgress(state, message);	
	}
	
	protected void setState(ServiceState state, Throwable error) {
		//TODO more readable error handling
		activity.changeProgress(state, error.getLocalizedMessage());
	}

	protected SharedPreferences getCredentials() {
		return activity.getSharedPreferences(Constants.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
	}
}
