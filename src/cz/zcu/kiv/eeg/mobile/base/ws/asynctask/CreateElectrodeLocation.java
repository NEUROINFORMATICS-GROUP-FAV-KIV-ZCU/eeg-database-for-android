package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeLocation;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Common service (Asynctask) for creating new Electrode Location on eeg base.
 *
 * @author Petr Miko
 */
public class CreateElectrodeLocation extends CommonService<ElectrodeLocation, Void, ElectrodeLocation> {

    private final static String TAG = CreateElectrodeLocation.class.getSimpleName();

    public final static int MESSAGE = R.string.working_ws_create_electrode_location;

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateElectrodeLocation(CommonActivity context) {
        super(context, MESSAGE);
    }

    /**
     * Method, where electrode location information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param electrodeLocations only one ElectrodeLocation object is accepted
     * @return information about created electrode location
     */
    @Override
    protected ElectrodeLocation doInBackground(ElectrodeLocation... electrodeLocations) {
        onServiceStart();
        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_ELECTRODE_LOCATIONS;


        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        requestHeaders.setContentType(MediaType.APPLICATION_XML);


        RestTemplate restTemplate = createRestClientInstance();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        ElectrodeLocation location = electrodeLocations[0];

        try {
            Log.d(TAG, url);

            HttpEntity<ElectrodeLocation> entity = new HttpEntity<ElectrodeLocation>(location, requestHeaders);
            // Make the network request
            return restTemplate.postForObject(url, entity, ElectrodeLocation.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            onServiceError(e);
        } finally {
            onServiceDone();
        }
        return null;
    }

    /**
     * Informs user whether ElectrodeLocation creation was successful or not.
     *
     * @param location returned electrode location info if any
     */
    @Override
    protected void onPostExecute(ElectrodeLocation location) {
        if (location != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_ELECTRODE_LOCATION_KEY, location);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
