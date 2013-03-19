package cz.zcu.kiv.eeg.mobile.base.ws.eegbase;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.ExperimentAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ExperimentList;
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
 * Common service (Asynctask) for fetching experiments from server.
 *
 * @author Petr Miko
 */
public class FetchExperiments extends CommonService<Void, Void, List<Experiment>> {

    private static final String TAG = FetchExperiments.class.getSimpleName();
    private ExperimentAdapter experimentAdapter;
    private String qualifier;

    /**
     * Constructor.
     *
     * @param activity          parent activity
     * @param experimentAdapter adapter for holding collection of experiments
     * @param qualifier         qualifier to distinguish whether to fetch private or public data
     */
    public FetchExperiments(CommonActivity activity, ExperimentAdapter experimentAdapter, String qualifier) {
        super(activity);
        this.experimentAdapter = experimentAdapter;
        this.qualifier = qualifier;
    }

    /**
     * Method, where all experiments are read from server.
     * All heavy lifting is made here.
     *
     * @param params not used (omitted) here
     * @return list of fetched experiments
     */
    @Override
    protected List<Experiment> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_EXPERIMENTS;

        //TODO HACK temporary solution
        if (Values.SERVICE_QUALIFIER_ALL.equals(qualifier)) {
            url += "public/" + Integer.MAX_VALUE;
        } else
            url += qualifier;

        setState(RUNNING, R.string.working_ws_experiments);
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
            ResponseEntity<ExperimentList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ExperimentList.class);
            ExperimentList body = response.getBody();

            if (body != null) {
                return body.getExperiments();
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
     * Read experiments are assigned to adapter here.
     *
     * @param resultList experiments fetched from server
     */
    @Override
    protected void onPostExecute(List<Experiment> resultList) {
        experimentAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Experiment>() {
                @Override
                public int compare(Experiment lhs, Experiment rhs) {
                    return lhs.getExperimentId() - rhs.getExperimentId();
                }
            });

            for (Experiment res : resultList) {
                experimentAdapter.add(res);
            }
        }
    }

}
