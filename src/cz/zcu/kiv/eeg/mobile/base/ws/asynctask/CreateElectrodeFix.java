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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Common service (Asynctask) for creating new Electrode Fix on eeg base.
 *
 * @author Petr Miko
 */
public class CreateElectrodeFix extends CommonService<ElectrodeFix, Void, ElectrodeFix> {

    private final static String TAG = CreateElectrodeFix.class.getSimpleName();
    private final static int MESSAGE = R.string.working_ws_create_electrode_fix;

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateElectrodeFix(CommonActivity context) {
        super(context, MESSAGE);
    }

    /**
     * Method, where electrode fix information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param electrodeFixes only one ElectrodeFix object is accepted
     * @return information about created electrode fix
     */
    @Override
    protected ElectrodeFix doInBackground(ElectrodeFix... electrodeFixes) {
        onServiceStart();

        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_ELECTRODE_FIXLIST;

        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        requestHeaders.setContentType(MediaType.APPLICATION_XML);


        RestTemplate restTemplate = createRestClientInstance();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        ElectrodeFix fix = electrodeFixes[0];

        try {
            Log.d(TAG, url);

            HttpEntity<ElectrodeFix> entity = new HttpEntity<ElectrodeFix>(fix, requestHeaders);
            // Make the network request
            return restTemplate.postForObject(url, entity, ElectrodeFix.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            onServiceError(e);
        } finally {
            onServiceDone();
        }
        return null;
    }

    /**
     * Informs user whether ElectrodeFix creation was successful or not.
     *
     * @param fix returned electrode fix info if any
     */
    @Override
    protected void onPostExecute(ElectrodeFix fix) {
        if (fix != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_ELECTRODE_FIX_KEY, fix);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
