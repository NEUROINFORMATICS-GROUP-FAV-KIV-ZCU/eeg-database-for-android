package cz.zcu.kiv.eeg.mobile.base.ws.eegbase;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.Experiment;
import cz.zcu.kiv.eeg.mobile.base.data.container.ExperimentAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ExperimentData;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ExperimentDataList;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ResearchGroupData;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ResearchGroupDataList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

public class FetchExperiments extends CommonService<Void, Void, List<ExperimentData>> {

    private static final String TAG = FetchExperiments.class.getSimpleName();

    private ExperimentAdapter experimentAdapter;

    public FetchExperiments(CommonActivity activity, ExperimentAdapter experimentAdapter) {
        super(activity);
        this.experimentAdapter = experimentAdapter;
    }

    @Override
    protected List<ExperimentData> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_USER + "experiments";

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
            ResponseEntity<ExperimentDataList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ExperimentDataList.class);
            ExperimentDataList body = response.getBody();

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

    @Override
    protected void onPostExecute(List<ExperimentData> resultList) {
        experimentAdapter.clear();
        if (resultList != null && !resultList.isEmpty()){
            for (ExperimentData res : resultList) {
                try {
                    Experiment experiment = new Experiment(res.getExperimentId(), res.getStartTime(), res.getEndTime(), res.getScenarioId(), res.getScenarioName());
                    experiment.setEnvironmentNote(res.getEnvironmentNote());
                    experimentAdapter.add(experiment);
                } catch (Exception e) {
                    setState(ERROR, e);
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }
            }
        }else{
                Experiment tmp = new Experiment(-Calendar.getInstance().get(Calendar.SECOND), new Date(), new Date(), -13, "Testing value, will fail if tried");
                experimentAdapter.add(tmp);
        }
    }

}
