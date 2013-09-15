package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeTypeAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeType;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeTypeList;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Common service (Asynctask) for fetching electrodeTypes from eeg base.
 *
 * @author Petr Miko
 */
public class FetchElectrodeTypes extends CommonService<Void, Void, List<ElectrodeType>> {

    private static final String TAG = FetchElectrodeTypes.class.getSimpleName();
    private final static int MESSAGE = R.string.working_ws_electrode_type;
    private final ElectrodeTypeAdapter electrodeTypeAdapter;

    /**
     * Constructor.
     *
     * @param activity             parent activity
     * @param electrodeTypeAdapter adapter for holding collection of electrodeTypes
     */
    public FetchElectrodeTypes(CommonActivity activity, ElectrodeTypeAdapter electrodeTypeAdapter) {
        super(activity, MESSAGE);
        this.electrodeTypeAdapter = electrodeTypeAdapter;
    }

    /**
     * Method, where all electrodeTypes are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched electrodeTypes
     */
    @Override
    protected List<ElectrodeType> doInBackground(Void... params) {
        onServiceStart();

        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_ELECTRODE_TYPES;

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
            ResponseEntity<ElectrodeTypeList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    ElectrodeTypeList.class);
            ElectrodeTypeList body = response.getBody();

            if (body != null) {
                return body.getElectrodeTypes();
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
     * Fetched records are put into ElectrodeType adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<ElectrodeType> resultList) {
        electrodeTypeAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ElectrodeType>() {
                @Override
                public int compare(ElectrodeType lhs, ElectrodeType rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (ElectrodeType electrodeType : resultList) {
                electrodeTypeAdapter.add(electrodeType);
            }
        }
    }

}
