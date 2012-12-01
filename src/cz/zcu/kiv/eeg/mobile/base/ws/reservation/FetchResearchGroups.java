package cz.zcu.kiv.eeg.mobile.base.ws.reservation;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

import java.util.Collections;
import java.util.List;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ResearchGroupData;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ResearchGroupDataList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;

import android.content.SharedPreferences;
import android.util.Log;

public class FetchResearchGroups extends CommonService<Void, Void, List<ResearchGroupData>> {

	private static final String TAG = FetchResearchGroups.class.getSimpleName();

	private ResearchGroupAdapter groupAdapter;

	public FetchResearchGroups(CommonActivity activity, ResearchGroupAdapter groupAdapter) {
		super(activity);
		this.groupAdapter = groupAdapter;
	}

	@Override
	protected List<ResearchGroupData> doInBackground(Void... params) {
		SharedPreferences credentials = getCredentials();
		String username = credentials.getString("username", null);
		String password = credentials.getString("password", null);
		String url = credentials.getString("url", null) + "/reservation/groups";

		setState(RUNNING, R.string.working_ws_groups);
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
			ResponseEntity<ResearchGroupDataList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
					ResearchGroupDataList.class);
			ResearchGroupDataList body = response.getBody();

			if (body != null) {
				return body.getGroups();
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
	protected void onPostExecute(List<ResearchGroupData> resultList) {
		groupAdapter.clear();
		if (resultList != null && !resultList.isEmpty())
			for (ResearchGroupData res : resultList) {
				ResearchGroup group = new ResearchGroup(res.getGroupId(), res.getGroupName());
				groupAdapter.add(group);
			}
	}

}
