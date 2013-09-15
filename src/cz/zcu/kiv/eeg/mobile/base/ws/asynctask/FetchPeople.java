package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.PersonAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Person;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.PersonList;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Common service (Asynctask) for fetching people from eeg base.
 *
 * @author Petr Miko
 */
public class FetchPeople extends CommonService<Void, Void, List<Person>> {

    private static final String TAG = FetchPeople.class.getSimpleName();
    private final static int MESSAGE = R.string.working_ws_people;
    private final PersonAdapter personAdapter;

    /**
     * Constructor.
     *
     * @param activity      parent activity
     * @param PersonAdapter adapter for holding collection of people
     */
    public FetchPeople(CommonActivity activity, PersonAdapter PersonAdapter) {
        super(activity, MESSAGE);
        this.personAdapter = PersonAdapter;
    }

    /**
     * Method, where all people are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched people
     */
    @Override
    protected List<Person> doInBackground(Void... params) {
        onServiceStart();

        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_USER + Values.SERVICE_QUALIFIER_ALL;

        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<Object> entity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = createRestClientInstance();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        try {
            // Make the network request
            Log.d(TAG, url);
            ResponseEntity<PersonList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    PersonList.class);
            PersonList body = response.getBody();

            if (body != null) {
                return body.getPeople();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            onServiceError(e);
        } finally {
            onServiceDone();
        }
        return Collections.emptyList();
    }

    /**
     * Fetched people are put into Person adapter and sorted.
     *
     * @param resultList fetched people
     */
    @Override
    protected void onPostExecute(List<Person> resultList) {
        personAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Person>() {
                @Override
                public int compare(Person lhs, Person rhs) {
                    return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                }
            });

            for (Person person : resultList) {
                personAdapter.add(person);
            }
        }
    }

}
