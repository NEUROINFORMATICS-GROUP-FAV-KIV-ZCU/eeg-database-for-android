package cz.zcu.kiv.eeg.mobile.base.archetypes;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.*;

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

    private final int message;

    /**
     * Constructor, which pairs CommonService and CommonActivity.
     *
     * @param context parent activity
     * @param message to be displayed when service is active
     */
    public CommonService(CommonActivity context, int message) {
        this.activity = context;
        this.message = message;
        ServiceReference.add(activity, CommonService.this, message);
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


    protected SharedPreferences getPreferences() {
        return activity.getPreferences();
    }

    public void executeCommonService(T... params) {
        this.execute(params);

    }

    protected final void onServiceStart() {
        activity.onServiceStart(message);
        Thread.currentThread().setName(this.getClass().getSimpleName());
    }


    /**
     * Hands over progress state change towards the activity.
     * Method tries to recognize throwable type for displaying custom message.
     * If throwable is not recognized, basic exception message is displayed.
     *
     * @param error error occurred during computation
     */
    protected final void onServiceError(final Throwable error) {

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
                Throwable exc = ((RestClientException) error).getRootCause();
                message = exc == null ? activity.getString(R.string.error_connection) : exc.getMessage();

                // attempt to recognize low-level connection errors
                if (message.contains("EHOSTUNREACH"))
                    message = activity.getString(R.string.error_host_unreach);
                else if (message.contains("ECONNREFUSED"))
                    message = activity.getString(R.string.error_host_con_refused);
                else if (message.contains("ETIMEDOUT"))
                    message = activity.getString(R.string.error_host_timeout);
            }

        }
        activity.onServiceError(this.message, message);
    }

    protected final void onServiceDone() {
        activity.onServiceDone(message);
    }

    protected final RestTemplate createRestClientInstance(){
        SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(60000);
        factory.setBufferRequestBody(false);
        // Create a new RestTemplate instance
        return new RestTemplate(factory);
    }
}
