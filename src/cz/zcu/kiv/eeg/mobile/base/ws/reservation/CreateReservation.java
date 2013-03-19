package cz.zcu.kiv.eeg.mobile.base.ws.reservation;

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
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

public class CreateReservation extends CommonService<Reservation, Void, Boolean> {

    private static final String TAG = CreateReservation.class.getSimpleName();
    private Reservation data;

    public CreateReservation(CommonActivity context) {
        super(context);
    }

    @Override
    protected Boolean doInBackground(Reservation... params) {

        data = params[0];

        // will be fixed properly in future
        if (data == null)
            return false;

        try {

            setState(RUNNING, R.string.working_ws_create);

            SharedPreferences credentials = getCredentials();
            String username = credentials.getString("username", null);
            String password = credentials.getString("password", null);
            String url = credentials.getString("url", null) + Values.SERVICE_RESERVATION;

            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
            requestHeaders.setContentType(MediaType.APPLICATION_XML);
            HttpEntity<Reservation> entity = new HttpEntity<Reservation>(data, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
            restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

            Log.d(TAG, url);
            ResponseEntity<Reservation> dataEntity = restTemplate.postForEntity(url, entity, Reservation.class);
            data = dataEntity.getBody();
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {

        if (success) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_RECORD_KEY, data);
            Toast.makeText(activity, activity.getString(R.string.reser_created), Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        }
    }
}
