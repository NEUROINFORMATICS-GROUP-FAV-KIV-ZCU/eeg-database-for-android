package cz.zcu.kiv.eeg.mobile.base.ws.db.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.WeatherAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.WeatherList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching weatherList from eeg base.
 *
 * @author Petr Miko
 */
public class FetchWeatherList extends CommonService<Void, Void, List<Weather>> {

    private static final String TAG = FetchWeatherList.class.getSimpleName();
    private final WeatherAdapter weatherAdapter;
    private int researchGroupId;

    /**
     * Constructor.
     *
     * @param activity       parent activity
     * @param weatherAdapter adapter for holding collection of weather
     */
    public FetchWeatherList(CommonActivity activity, int researchGroupId, WeatherAdapter weatherAdapter) {
        super(activity);
        this.weatherAdapter = weatherAdapter;
        this.researchGroupId = researchGroupId;
    }

    /**
     * Method, where all weatherList are read from server.
     * All heavy lifting is made here.
     *
     * @param params parameters - omitted here
     * @return list of fetched weather
     */
    @Override
    protected List<Weather> doInBackground(Void... params) {

        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_WEATHER + "/" + researchGroupId;

        setState(RUNNING, R.string.working_ws_weather);
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
            ResponseEntity<WeatherList> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    WeatherList.class);
            WeatherList body = response.getBody();

            if (body != null) {
                return body.getWeatherList();
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
     * Fetched records are put into Weather adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Weather> resultList) {
        weatherAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Weather>() {
                @Override
                public int compare(Weather lhs, Weather rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getWeatherId() - rhs.getWeatherId();
                }
            });

            for (Weather artifact : resultList) {
                weatherAdapter.add(artifact);
            }
        }
    }

}
