package cz.zcu.kiv.eeg.mobile.base.ws.eegbase;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.ws.data.PersonData;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ScenarioData;
import cz.zcu.kiv.eeg.mobile.base.ws.data.UserInfo;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * @author Petr Miko
 */
public class CreateScenario extends CommonService<ScenarioData, Void, URI> {

    private final static String TAG = CreateScenario.class.getSimpleName();

    public CreateScenario(CommonActivity context) {
        super(context);
    }

    @Override
    protected URI doInBackground(ScenarioData... scenarioDatas) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_SCENARIOS;

        setState(RUNNING, R.string.working_ws_create_scenario);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));


        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());


        ScenarioData scenario = scenarioDatas[0];

        try {
            Log.d(TAG, url);
            FileSystemResource toBeUploadedFile = new FileSystemResource(scenario.getFilePath());
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
            form.add("scenarioName", scenario.getScenarioName());
            form.add("researchGroupId", Integer.toString(scenario.getResearchGroupId()));
            form.add("description", scenario.getDescription());
            form.add("mimeType", scenario.getMimeType());
            form.add("private", Boolean.toString(scenario.isPrivate()));
            form.add("file", toBeUploadedFile);

            HttpEntity<Object> entity = new HttpEntity<Object>(form, requestHeaders);
            // Make the network request
            return restTemplate.postForLocation(url, entity, UserInfo.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    @Override
    protected void onPostExecute(URI uri) {
        if (uri != null) {
            Toast.makeText(activity, "Scenario was successfully created and is now available on location:\n " + uri.toASCIIString(), Toast.LENGTH_SHORT).show();
            activity.finish();

        } else {
            Toast.makeText(activity, "Scenario creation was unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}
