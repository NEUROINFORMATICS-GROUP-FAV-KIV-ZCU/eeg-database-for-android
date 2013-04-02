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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for creating new Artifact on eeg base.
 *
 * @author Petr Miko
 */
public class CreateArtifact extends CommonService<Artifact, Void, Artifact> {

    private final static String TAG = CreateArtifact.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateArtifact(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where artifact information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param artifacts only one Artifact object is accepted
     * @return information about created artifact
     */
    @Override
    protected Artifact doInBackground(Artifact... artifacts) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_ARTIFACTS;

        setState(RUNNING, R.string.working_ws_create_artifact);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        requestHeaders.setContentType(MediaType.APPLICATION_XML);


        SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        Artifact artifact = artifacts[0];

        try {
            Log.d(TAG, url);

            HttpEntity<Artifact> entity = new HttpEntity<Artifact>(artifact, requestHeaders);
            // Make the network request
            return restTemplate.postForObject(url, entity, Artifact.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    /**
     * Informs user whether artifact creation was successful or not.
     *
     * @param artifact returned artifact info if any
     */
    @Override
    protected void onPostExecute(Artifact artifact) {
        if (artifact != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_ARTIFACT_KEY, artifact);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
