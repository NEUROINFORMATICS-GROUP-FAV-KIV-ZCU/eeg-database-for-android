package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.DigitizationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.DigitizationList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching digitizations from eeg base.
 *
 * @author Petr Miko
 */
public class FetchDigitizations extends CommonService<Void, Void, List<Digitization>> {

    private static final String TAG = FetchDigitizations.class.getSimpleName();
    private final DigitizationAdapter digitizationAdapter;

    /**
     * Constructor.
     *
     * @param activity            parent activity
     * @param digitizationAdapter adapter for holding collection of digitizations
     */
    public FetchDigitizations(CommonActivity activity, DigitizationAdapter digitizationAdapter) {
        super(activity);
        this.digitizationAdapter = digitizationAdapter;
    }

    /**
     * Method, where all digitizations are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched digitizations
     */
    @Override
    protected List<Digitization> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_DIGITIZATIONS;

        setState(RUNNING, R.string.working_ws_digitization);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<Object> entity = new HttpEntity<Object>(requestHeaders);

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        try {
            // Make the network request
            Log.d(TAG, url);
            ResponseEntity<DigitizationList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    DigitizationList.class);
            DigitizationList body = response.getBody();

            if (body != null) {
                return body.getDigitizations();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return Collections.emptyList();
    }

    /**
     * Fetched records are put into Digitization adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Digitization> resultList) {
        digitizationAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Digitization>() {
                @Override
                public int compare(Digitization lhs, Digitization rhs) {
                    return lhs.getFilter().compareTo(rhs.getFilter());
                }
            });

            for (Digitization artifact : resultList) {
                digitizationAdapter.add(artifact);
            }
        }
    }

}
