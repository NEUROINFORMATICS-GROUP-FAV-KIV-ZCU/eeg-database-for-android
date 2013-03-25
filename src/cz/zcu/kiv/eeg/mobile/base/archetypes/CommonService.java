package cz.zcu.kiv.eeg.mobile.base.archetypes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.ServiceState;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

/**
 * Parent for async tasks used in this application.
 * Meant for direct cooperation with CommonActivity and relies on its setState capabilities.
 *
 * @param <T> parameters type
 * @param <U> progress units type
 * @param <V> result object type
 */
public abstract class CommonService<T, U, V> extends AsyncTask<T, U, V> {

    /**
     * Activity paired with asynctask.
     */
    protected CommonActivity activity;

    /**
     * Constructor, which pairs CommonService and CommonActivity.
     *
     * @param context parent activity
     */
    public CommonService(CommonActivity context) {
        //explicit reference between activity and service must be set for new onCreate activity refreshes
        ServiceReference.push(context, this);
    }

    /**
     * Hands over progress state change towards the activity.
     *
     * @param state new service state
     */
    protected void setState(ServiceState state) {
        activity.changeProgress(state);
    }

    /**
     * Hands over progress state change towards the activity.
     *
     * @param state       new service state
     * @param messageCode android string identifier
     */
    protected void setState(ServiceState state, int messageCode) {
        activity.changeProgress(state, activity.getString(messageCode));
    }

    /**
     * Hands over progress state change towards the activity.
     *
     * @param state   new service state
     * @param message message
     */
    protected void setState(ServiceState state, String message) {
        activity.changeProgress(state, message);
    }

    /**
     * Hands over progress state change towards the activity.
     * Method tries to recognize throwable type for displaying custom message.
     * If throwable is not recognized, basic exception message is displayed.
     *
     * @param state new service state
     * @param error error occurred during computation
     */
    protected void setState(ServiceState state, Throwable error) {

        String message = error.getMessage() == null ? activity.getString(R.string.error_connection) : error.getMessage();
        if (error instanceof RestClientException) {

            //error on client side
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
                }
                //error on server side
            } else if (error instanceof HttpServerErrorException) {
                HttpStatus status = ((HttpServerErrorException) error).getStatusCode();

                switch (status) {
                    case INTERNAL_SERVER_ERROR:
                        message = activity.getString(R.string.error_http_500) + "\n " + error.getMessage();
                        break;
                    case SERVICE_UNAVAILABLE:
                        message = activity.getString(R.string.error_http_503);
                        break;
                }
                //connection broke
            } else if (error instanceof ResourceAccessException) {
                message = activity.getString(R.string.error_ssl);
            } else {
                error = ((RestClientException) error).getRootCause();
                message = error == null ? activity.getString(R.string.error_connection) : error.getMessage();

                // attempt to recognize low-level connection errors
                if (message.contains("EHOSTUNREACH"))
                    message = activity.getString(R.string.error_host_unreach);
                else if (message.contains("ECONNREFUSED"))
                    message = activity.getString(R.string.error_host_con_refused);
                else if (message.contains("ETIMEDOUT"))
                    message = activity.getString(R.string.error_host_timeout);
            }

        }
        //display the error message
        activity.changeProgress(state, message);
    }

    /**
     * Set parent activity.
     * Usually used when activity got recreated but we need to keep service paired with displayed instance.
     *
     * @param activity new parent CommonActivity
     */
    public void setActivity(CommonActivity activity) {
        this.activity = activity;
    }

    /**
     * Credentials information bundle getter.
     *
     * @return credentials information bundle
     */
    protected SharedPreferences getCredentials() {
        return activity.getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
    }
}
