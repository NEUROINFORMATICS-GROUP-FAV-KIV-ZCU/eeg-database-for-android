package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroupList;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Service (AsyncTask) for fetching user's research groups from eeg base.
 *
 * @author Petr Miko
 */
public class FetchResearchGroups extends CommonService<Void, Void, List<ResearchGroup>> {

    private static final String TAG = FetchResearchGroups.class.getSimpleName();
    private final static int MESSAGE = R.string.working_ws_groups;
    private final ResearchGroupAdapter groupAdapter;
    private final String qualifier;

    /**
     * Constructor.
     *
     * @param activity     parent activity
     * @param groupAdapter adapter where should be stored fetched research groups
     * @param qualifier    qualifier (whether to fetch private or public records)
     */
    public FetchResearchGroups(CommonActivity activity, ResearchGroupAdapter groupAdapter, String qualifier) {
        super(activity, MESSAGE);
        this.groupAdapter = groupAdapter;
        this.qualifier = qualifier;
    }

    /**
     * Method, where all research groups are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched research groups
     */
    @Override
    protected List<ResearchGroup> doInBackground(Void... params) {
        onServiceStart();

        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_RESEARCH_GROUPS + qualifier;

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
            ResponseEntity<ResearchGroupList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ResearchGroupList.class);
            ResearchGroupList body = response.getBody();

            if (body != null) {
                return body.getGroups();
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
     * Fetched research groups are stored into adapter.
     *
     * @param resultList fetched research groups
     */
    @Override
    protected void onPostExecute(List<ResearchGroup> resultList) {
        groupAdapter.clear();
        if (resultList != null && !resultList.isEmpty())
            for (ResearchGroup res : resultList) {
                groupAdapter.add(res);
            }
    }

}
