package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.DiseaseAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.DiseaseList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching diseases from eeg base.
 *
 * @author Petr Miko
 */
public class FetchDiseases extends CommonService<Void, Void, List<Disease>> {

    private static final String TAG = FetchDiseases.class.getSimpleName();
    private final DiseaseAdapter diseaseAdapter;

    /**
     * Constructor.
     *
     * @param activity       parent activity
     * @param diseaseAdapter adapter for holding collection of diseases
     */
    public FetchDiseases(CommonActivity activity, DiseaseAdapter diseaseAdapter) {
        super(activity);
        this.diseaseAdapter = diseaseAdapter;
    }

    /**
     * Method, where all diseases are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched diseases
     */
    @Override
    protected List<Disease> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_DISEASES;

        setState(RUNNING, R.string.working_ws_disease);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<Object> entity = new HttpEntity<Object>(requestHeaders);

        SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        try {
            // Make the network request
            Log.d(TAG, url);
            ResponseEntity<DiseaseList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    DiseaseList.class);
            DiseaseList body = response.getBody();

            if (body != null) {
                return body.getDiseases();
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
     * Fetched records are put into Disease adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Disease> resultList) {
        diseaseAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Disease>() {
                @Override
                public int compare(Disease lhs, Disease rhs) {
                    int sub = lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getDiseaseId() - rhs.getDiseaseId();
                }
            });

            for (Disease disease : resultList) {
                diseaseAdapter.add(disease);
            }
        }
    }

}
