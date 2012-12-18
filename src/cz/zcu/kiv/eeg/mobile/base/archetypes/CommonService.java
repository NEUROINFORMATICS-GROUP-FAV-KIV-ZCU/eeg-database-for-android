package cz.zcu.kiv.eeg.mobile.base.archetypes;

import cz.zcu.kiv.eeg.mobile.base.data.Constants;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.lang.ref.SoftReference;

public abstract class CommonService<T, U, V> extends AsyncTask<T, U, V> {

	protected SoftReference<CommonActivity> activity;

	public CommonService(CommonActivity context) {
		this.activity = new SoftReference<CommonActivity>(context);
	}
	
	protected void setState(ServiceState state){
		activity.get().changeProgress(state);
	}
	
	protected void setState(ServiceState state, int messageCode){
        activity.get().changeProgress(state, activity.get().getString(messageCode));
	}
	
	protected void setState(ServiceState state, String message) {
        activity.get().changeProgress(state, message);
	}
	
	protected void setState(ServiceState state, Throwable error) {
		//TODO more readable error handling
        activity.get().changeProgress(state, error.getLocalizedMessage());
	}

    public void setActivity(CommonActivity activity){
        this.activity = new SoftReference<CommonActivity>(activity);
    }

	protected SharedPreferences getCredentials() {
		return activity.get().getSharedPreferences(Constants.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
	}
}
