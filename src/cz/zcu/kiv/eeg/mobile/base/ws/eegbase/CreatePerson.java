package cz.zcu.kiv.eeg.mobile.base.ws.eegbase;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Person;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.UserInfo;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * @author Petr Miko
 */
public class CreatePerson extends CommonService<Person, Void, UserInfo> {

    private final static String TAG = CreatePerson.class.getSimpleName();

    public CreatePerson(CommonActivity context) {
        super(context);
    }

    @Override
    protected UserInfo doInBackground(Person... persons) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_USER + "create";

        setState(RUNNING, R.string.working_ws_create_user);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        requestHeaders.setContentType(MediaType.APPLICATION_XML);


        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        Person person = persons[0];

        try {
            Log.d(TAG, url);

            HttpEntity<Person> entity = new HttpEntity<Person>(person, requestHeaders);
            // Make the network request
            return restTemplate.postForObject(url, entity, UserInfo.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    @Override
    protected void onPostExecute(UserInfo userInfo) {
        if (userInfo != null) {
            Toast.makeText(activity, "Person was successfully created with rights:\n " + userInfo.getRights(), Toast.LENGTH_SHORT).show();
            activity.finish();

        } else {
            Toast.makeText(activity, "User creation was unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}
