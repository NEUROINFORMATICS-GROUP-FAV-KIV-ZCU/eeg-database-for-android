package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.HardwareAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Hardware;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.HardwareList;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Common service (Asynctask) for fetching hardwareList from eeg base.
 *
 * @author Petr Miko
 */
public class FetchHardwareList extends CommonService<Void, Void, List<Hardware>> {

    private static final String TAG = FetchHardwareList.class.getSimpleName();
    private final static int MESSAGE = R.string.working_ws_hardware;
    private final HardwareAdapter hardwareAdapter;

    /**
     * Constructor.
     *
     * @param activity        parent activity
     * @param hardwareAdapter adapter for holding collection of hardware
     */
    public FetchHardwareList(CommonActivity activity, HardwareAdapter hardwareAdapter) {
        super(activity, MESSAGE);
        this.hardwareAdapter = hardwareAdapter;
    }

    /**
     * Method, where all hardwareList are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched hardware
     */
    @Override
    protected List<Hardware> doInBackground(Void... params) {
        onServiceStart();

        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_HARDWARE;

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
            ResponseEntity<HardwareList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    HardwareList.class);
            HardwareList body = response.getBody();

            if (body != null) {
                return body.getHardwareList();
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
     * Fetched records are put into Hardware adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Hardware> resultList) {
        hardwareAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Hardware>() {
                @Override
                public int compare(Hardware lhs, Hardware rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getHardwareId() - rhs.getHardwareId();
                }
            });

            for (Hardware artifact : resultList) {
                hardwareAdapter.add(artifact);
            }
        }
    }

}
