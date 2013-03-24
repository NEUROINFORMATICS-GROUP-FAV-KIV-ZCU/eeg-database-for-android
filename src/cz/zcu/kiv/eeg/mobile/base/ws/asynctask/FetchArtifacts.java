package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.ArtifactAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ArtifactList;
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
 * Common service (Asynctask) for fetching artifacts from eeg base.
 *
 * @author Petr Miko
 */
public class FetchArtifacts extends CommonService<Void, Void, List<Artifact>> {

    private static final String TAG = FetchArtifacts.class.getSimpleName();
    private final ArtifactAdapter artifactAdapter;

    /**
     * Constructor.
     *
     * @param activity        parent activity
     * @param artifactAdapter adapter for holding collection of artifacts
     */
    public FetchArtifacts(CommonActivity activity, ArtifactAdapter artifactAdapter) {
        super(activity);
        this.artifactAdapter = artifactAdapter;
    }

    /**
     * Method, where all artifacts are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched artifacts
     */
    @Override
    protected List<Artifact> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_ARTIFACTS;

        setState(RUNNING, R.string.working_ws_artifacts);
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
            ResponseEntity<ArtifactList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ArtifactList.class);
            ArtifactList body = response.getBody();

            if (body != null) {
                return body.getArtifacts();
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
     * Fetched records are put into Artifact adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Artifact> resultList) {
        artifactAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Artifact>() {
                @Override
                public int compare(Artifact lhs, Artifact rhs) {
                    return lhs.getArtifactId() - rhs.getArtifactId();
                }
            });

            for (Artifact artifact : resultList) {
                artifactAdapter.add(artifact);
            }
        }
    }

}
