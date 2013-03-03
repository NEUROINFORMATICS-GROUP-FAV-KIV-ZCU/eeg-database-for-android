package cz.zcu.kiv.eeg.mobile.base.ws.eegbase;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.Scenario;
import cz.zcu.kiv.eeg.mobile.base.data.container.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ScenarioData;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ScenarioDataList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

public class FetchScenarios extends CommonService<Void, Void, List<ScenarioData>> {

    private static final String TAG = FetchScenarios.class.getSimpleName();
    private final ScenarioAdapter scenarioAdapter;
    private final String qualifier;

    public FetchScenarios(CommonActivity activity, ScenarioAdapter scenarioAdapter, String qualifier) {
        super(activity);
        this.scenarioAdapter = scenarioAdapter;
        this.qualifier = qualifier;
    }

    @Override
    protected void onPreExecute() {
        scenarioAdapter.clear();
        scenarioAdapter.notifyDataSetChanged();
    }

    @Override
    protected List<ScenarioData> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_SCENARIOS + qualifier;

        setState(RUNNING, R.string.working_ws_scenarios);
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
            ResponseEntity<ScenarioDataList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ScenarioDataList.class);
            ScenarioDataList body = response.getBody();

            if (body != null) {
                return body.getScenarios();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return Collections.emptyList();
    }

    @Override
    protected void onPostExecute(List<ScenarioData> resultList) {

        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ScenarioData>() {
                @Override
                public int compare(ScenarioData lhs, ScenarioData rhs) {
                    return lhs.getScenarioId() - rhs.getScenarioId();
                }
            });

            for (ScenarioData res : resultList) {
                try {
                    Scenario scenario = new Scenario();
                    scenario.setScenarioId(res.getScenarioId());
                    scenario.setScenarioName(res.getScenarioName());
                    scenario.setDescription(res.getDescription());
                    scenario.setFileLength(res.getFileLength());
                    scenario.setFileName(res.getFileName());
                    scenario.setMimeType(res.getMimeType());
                    scenario.setOwnerName(res.getOwnerName());
                    scenario.setResearchGroupName(res.getResearchGroupName());
                    scenario.setPrivate(res.isPrivate());

                    scenarioAdapter.add(scenario);
                } catch (Exception e) {
                    setState(ERROR, e);
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }
            }
        }
    }

}
