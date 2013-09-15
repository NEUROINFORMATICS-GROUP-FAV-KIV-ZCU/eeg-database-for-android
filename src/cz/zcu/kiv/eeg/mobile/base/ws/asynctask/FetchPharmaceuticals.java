package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.PharmaceuticalAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Pharmaceutical;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.PharmaceuticalList;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Common service (Asynctask) for fetching pharmaceuticals from eeg base.
 *
 * @author Petr Miko
 */
public class FetchPharmaceuticals extends CommonService<Void, Void, List<Pharmaceutical>> {

    private static final String TAG = FetchPharmaceuticals.class.getSimpleName();
    private final static int MESSAGE = R.string.working_ws_pharmaceutical;
    private final PharmaceuticalAdapter pharmaceuticalAdapter;

    /**
     * Constructor.
     *
     * @param activity              parent activity
     * @param pharmaceuticalAdapter adapter for holding collection of pharmaceuticals
     */
    public FetchPharmaceuticals(CommonActivity activity, PharmaceuticalAdapter pharmaceuticalAdapter) {
        super(activity, MESSAGE);
        this.pharmaceuticalAdapter = pharmaceuticalAdapter;
    }

    /**
     * Method, where all pharmaceuticals are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched pharmaceuticals
     */
    @Override
    protected List<Pharmaceutical> doInBackground(Void... params) {
        onServiceStart();

        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_PHARMACEUTICAL;

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
            ResponseEntity<PharmaceuticalList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    PharmaceuticalList.class);
            PharmaceuticalList body = response.getBody();

            if (body != null) {
                return body.getPharmaceuticals();
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
     * Fetched records are put into Pharmaceutical adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Pharmaceutical> resultList) {
        pharmaceuticalAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Pharmaceutical>() {
                @Override
                public int compare(Pharmaceutical lhs, Pharmaceutical rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (Pharmaceutical pharmaceutical : resultList) {
                pharmaceuticalAdapter.add(pharmaceutical);
            }
        }
    }

}
