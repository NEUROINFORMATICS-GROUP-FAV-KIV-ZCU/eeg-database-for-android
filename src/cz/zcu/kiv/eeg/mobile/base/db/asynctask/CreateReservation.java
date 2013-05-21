package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (AsyncTask) for creating reservation on eeg base.
 *
 * @author Petr Miko
 */
public class CreateReservation extends CommonService<Reservation, Void, Reservation> {

    private static final String TAG = CreateReservation.class.getSimpleName();

    /**
     * Constructor.
     *
     * @param context parent activity
     */
    public CreateReservation(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where reservation information is pushed to server in order to create reservation record.
     * All heavy lifting is made here.
     *
     * @param params only one reservation instance is allowed here - reservation to be created
     * @return object of created reservation if any
     */
    @Override
    protected Reservation doInBackground(Reservation... params) {

        Reservation data = params[0];
        try {

            setState(RUNNING, R.string.working_ws_create);

            SharedPreferences credentials = getCredentials();
            String username = credentials.getString("username", null);
            String password = credentials.getString("password", null);
            String url = credentials.getString("url", null) + Values.SERVICE_RESERVATION;

            //set HTTP connection
            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
            requestHeaders.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<Reservation> entity = new HttpEntity<Reservation>(data, requestHeaders);

            SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate(factory);
            restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

            Log.d(TAG, url);
            ResponseEntity<Reservation> dataEntity = restTemplate.postForEntity(url, entity, Reservation.class);
            return dataEntity.getBody();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    /**
     * If reservation was created
     *
     * @param reservation created reservation if any
     */
    @Override
    protected void onPostExecute(Reservation reservation) {

        if (reservation != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_RESERVATION_KEY, reservation);
            Toast.makeText(activity, activity.getString(R.string.reser_created), Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        }
    }
}
