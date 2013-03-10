package cz.zcu.kiv.eeg.mobile.base.ws.eegbase;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.*;
import cz.zcu.kiv.eeg.mobile.base.ws.data.*;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

public class FetchExperiments extends CommonService<Void, Void, List<ExperimentData>> {

    private static final String TAG = FetchExperiments.class.getSimpleName();
    private ExperimentAdapter experimentAdapter;
    private String qualifier;

    public FetchExperiments(CommonActivity activity, ExperimentAdapter experimentAdapter, String qualifier) {
        super(activity);
        this.experimentAdapter = experimentAdapter;
        this.qualifier = qualifier;
    }

    @Override
    protected List<ExperimentData> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_EXPERIMENTS;

        //TODO HACK temporary solution
        if(Values.SERVICE_QUALIFIER_ALL.equals(qualifier)){
           url+= "public/"+Integer.MAX_VALUE;
        }   else
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
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ExperimentData>() {
                @Override
                public int compare(ExperimentData lhs, ExperimentData rhs) {
                    return lhs.getExperimentId() - rhs.getExperimentId();
                }
            });

            for (ExperimentData res : resultList) {
                try {
                    Experiment experiment = new Experiment(res.getExperimentId(), res.getStartTime(), res.getEndTime(), res.getScenario().getScenarioId(), res.getScenario().getScenarioName());
                    SubjectData s = res.getSubject();
                    Person subject = new Person();
                    subject.setId(s.getPersonId());
                    subject.setName(s.getName());
                    subject.setSurname(s.getSurname());
                    subject.setGender(s.getGender());
                    subject.setAge(s.getAge());
                    subject.setLeftHanded(s.isLeftHanded());


                    WeatherData w = res.getWeather();
                    if (w != null) {
                        Weather weather = new Weather();
                        weather.setId(w.getWeatherId());
                        weather.setTitle(w.getTitle());
                        weather.setDescription(w.getDescription());
                        experiment.setWeather(weather);
                    }

                    List<DiseaseData> dl = res.getDiseases() != null ? res.getDiseases().getDiseases() : null;
                    if(dl != null && !dl.isEmpty()){
                    List<Disease> diseases = new ArrayList<Disease>(dl.size());
                        for(DiseaseData d : dl){
                            Disease disease = new Disease();
                            disease.setId(d.getDiseaseId());
                            disease.setName(d.getName());
                            disease.setDescription(d.getDescription());
                            diseases.add(disease);
                        }
                     experiment.setDiseases(diseases);
                    }

                    ArtifactData ad = res.getArtifact();
                    if(ad != null){
                        Artifact artifact = new Artifact();
                        artifact.setArtifactId(ad.getArtifactId());
                        artifact.setCompensation(ad.getCompensation());
                        artifact.setRejectCondition(ad.getRejectCondition());
                        experiment.setArtifact(artifact);
                    }

                    DigitizationData digid = res.getDigitization();
                    if(digid != null){
                        Digitization digitization = new Digitization();
                        digitization.setDigitizationId(digid.getDigitizationId());
                        digitization.setFilter(digid.getFilter());
                        digitization.setGain(digid.getGain());
                        digitization.setSamplingRate(digid.getSamplingRate());
                        experiment.setDigitization(digitization);
                    }

                    experiment.setSubject(subject);
                    experiment.setEnvironmentNote(res.getEnvironmentNote());
                    experimentAdapter.add(experiment);
                } catch (Exception e) {
                    setState(ERROR, e);
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }
            }
        }
    }

}
