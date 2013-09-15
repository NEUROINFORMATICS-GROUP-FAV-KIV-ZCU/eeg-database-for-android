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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Person;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Common service (Asynctask) for creating new Person on eeg base.
 * Meant mainly for creating new subject.
 *
 * @author Petr Miko
 */
public class CreatePerson extends CommonService<Person, Void, Person> {

    private final static String TAG = CreatePerson.class.getSimpleName();
    private final static int MESSAGE = R.string.working_ws_create_user;

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreatePerson(CommonActivity context) {
        super(context, MESSAGE);
    }

    /**
     * Method, where person information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param persons only one Person object is accepted
     * @return information about created user
     */
    @Override
    protected Person doInBackground(Person... persons) {
        onServiceStart();

        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_USER + "create";

        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        requestHeaders.setContentType(MediaType.APPLICATION_XML);


        RestTemplate restTemplate = createRestClientInstance();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        Person person = persons[0];

        try {
            Log.d(TAG, url);

            HttpEntity<Person> entity = new HttpEntity<Person>(person, requestHeaders);
            // Make the network request
            return restTemplate.postForObject(url, entity, Person.class);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            onServiceError(e);
        } finally {
            onServiceDone();
        }
        return null;
    }

    /**
     * Informs user whether person creation was successful or not.
     *
     * @param person returned user info if any
     */
    @Override
    protected void onPostExecute(Person person) {
        if (person != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_PERSON_KEY, person);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
