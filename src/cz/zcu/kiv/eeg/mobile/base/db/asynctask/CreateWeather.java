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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for creating new Weather on eeg base.
 *
 * @author Petr Miko
 */
public class CreateWeather extends CommonService<Weather, Void, Weather> {

    private final static String TAG = CreateWeather.class.getSimpleName();
    private int researchGroupId;

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateWeather(CommonActivity context, int researchGroupId) {
        super(context);
        this.researchGroupId = researchGroupId;
    }

    /**
     * Method, where Weather information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param weathers only one Weather object is accepted
     * @return information about created weather
     */
    @Override
    protected Weather doInBackground(Weather... weathers) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_WEATHER + "/" + researchGroupId;

        setState(RUNNING, R.string.working_ws_create_weather);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        requestHeaders.setContentType(MediaType.APPLICATION_XML);


        SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        Weather weather = weathers[0];

        try {
            Log.d(TAG, url);

            HttpEntity<Weather> entity = new HttpEntity<Weather>(weather, requestHeaders);
            // Make the network request
            return restTemplate.postForObject(url, entity, Weather.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    /**
     * Informs user whether weather creation was successful or not.
     *
     * @param weather returned weather info if any
     */
    @Override
    protected void onPostExecute(Weather weather) {
        if (weather != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_WEATHER_KEY, weather);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
