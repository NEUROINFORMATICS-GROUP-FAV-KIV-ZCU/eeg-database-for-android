package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

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
 * Common service (Asynctask) for creating new Person on eeg base.
 * Meant mainly for creating new subject.
 *
 * @author Petr Miko
 */
public class CreatePerson extends CommonService<Person, Void, UserInfo> {

    private final static String TAG = CreatePerson.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreatePerson(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where person information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param persons only one Person object is accepted
     * @return information about created user
     */
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

    /**
     * Informs user whether person creation was successful or not.
     *
     * @param userInfo returned user info if any
     */
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
