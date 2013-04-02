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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for creating new Experiment on eeg base.
 *
 * @author Petr Miko
 */
public class CreateExperiment extends CommonService<Experiment, Void, Experiment> {

    private final static String TAG = CreateExperiment.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateExperiment(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where Experiment information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param experiments only one Experiment object is accepted
     * @return information about created experiment
     */
    @Override
    protected Experiment doInBackground(Experiment... experiments) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_EXPERIMENTS + "create";

        setState(RUNNING, R.string.working_ws_create_experiment);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        requestHeaders.setContentType(MediaType.APPLICATION_XML);


        SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        Experiment experiment = experiments[0];

        try {
            Log.d(TAG, url);

            HttpEntity<Experiment> entity = new HttpEntity<Experiment>(experiment, requestHeaders);
            // Make the network request
            return restTemplate.postForObject(url, entity, Experiment.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    /**
     * Informs user whether experiment creation was successful or not.
     *
     * @param experiment returned experiment info if any
     */
    @Override
    protected void onPostExecute(Experiment experiment) {
        if (experiment != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_EXPERIMENT_KEY, experiment);
            Toast.makeText(activity, R.string.creation_experiment_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
