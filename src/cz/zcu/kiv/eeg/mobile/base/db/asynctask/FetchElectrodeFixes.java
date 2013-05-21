package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeFixAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFixList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching electrodeFixs from eeg base.
 *
 * @author Petr Miko
 */
public class FetchElectrodeFixes extends CommonService<Void, Void, List<ElectrodeFix>> {

    private static final String TAG = FetchElectrodeFixes.class.getSimpleName();
    private final ElectrodeFixAdapter electrodeFixAdapter;

    /**
     * Constructor.
     *
     * @param activity            parent activity
     * @param electrodeFixAdapter adapter for holding collection of electrodeFixes
     */
    public FetchElectrodeFixes(CommonActivity activity, ElectrodeFixAdapter electrodeFixAdapter) {
        super(activity);
        this.electrodeFixAdapter = electrodeFixAdapter;
    }

    /**
     * Method, where all electrodeTypes are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched electrodeFixes
     */
    @Override
    protected List<ElectrodeFix> doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_ELECTRODE_FIXLIST;

        setState(RUNNING, R.string.working_ws_electrode_fix);
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
            ResponseEntity<ElectrodeFixList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ElectrodeFixList.class);
            ElectrodeFixList body = response.getBody();

            if (body != null) {
                return body.getElectrodeFixList();
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
     * Fetched records are put into ElectrodeFix adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<ElectrodeFix> resultList) {
        electrodeFixAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ElectrodeFix>() {
                @Override
                public int compare(ElectrodeFix lhs, ElectrodeFix rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (ElectrodeFix electrodeFix : resultList) {
                electrodeFixAdapter.add(electrodeFix);
            }
        }
    }

}
