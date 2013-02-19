package cz.zcu.kiv.eeg.mobile.base.archetypes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.lang.ref.SoftReference;

public abstract class CommonService<T, U, V> extends AsyncTask<T, U, V> {

    protected CommonActivity activity;

    public CommonService(CommonActivity context) {
        this.activity = context;
    }

    protected void setState(ServiceState state) {
        activity.changeProgress(state);
    }

    protected void setState(ServiceState state, int messageCode) {
        activity.changeProgress(state, activity.getString(messageCode));
    }

    protected void setState(ServiceState state, String message) {
        activity.changeProgress(state, message);
    }

    protected void setState(ServiceState state, Throwable error) {

        String message = error.getMessage() == null ? activity.getString(R.string.error_connection) : error.getMessage();
        if (error instanceof RestClientException) {

            if (error instanceof HttpClientErrorException) {
                HttpStatus status = ((HttpClientErrorException) error).getStatusCode();

                switch (status) {
                    case BAD_REQUEST:
                        message = activity.getString(R.string.error_http_400);
                        break;
                    case UNAUTHORIZED:
                        message = activity.getString(R.string.error_http_401);
                        break;
                    case FORBIDDEN:
                        message = activity.getString(R.string.error_http_403);
                        break;
                    case NOT_FOUND:
                        message = activity.getString(R.string.error_http_404);
                        break;
                    case METHOD_NOT_ALLOWED:
                        message = activity.getString(R.string.error_http_405);
                        break;
                    case REQUEST_TIMEOUT:
                        message = activity.getString(R.string.error_http_408);
                        break;
                    case INTERNAL_SERVER_ERROR:
                        message = activity.getString(R.string.error_http_500);
                        break;
                    case SERVICE_UNAVAILABLE:
                        message = activity.getString(R.string.error_http_503);
                        break;
                }
            } else {
                error = ((RestClientException) error).getRootCause();
                message = error.getMessage() == null ? activity.getString(R.string.error_connection) : error.getMessage();
                if (message.contains("EHOSTUNREACH"))
                    message = activity.getString(R.string.error_host_unreach);
                else if (message.contains("ECONNREFUSED"))
                    message = activity.getString(R.string.error_host_con_refused);
                else if (message.contains("ETIMEDOUT"))
                    message = activity.getString(R.string.error_host_timeout);
            }
        }
        activity.changeProgress(state, message);
    }

    public void setActivity(CommonActivity activity) {
        this.activity = activity;
    }

    protected SharedPreferences getCredentials() {
        return activity.getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
    }
}
