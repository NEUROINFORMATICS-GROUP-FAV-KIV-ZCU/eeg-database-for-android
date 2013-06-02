package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.WeatherAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
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
        setState(RUNNING, R.string.working_ws_weather);
        List<?> results = new ArrayList<Object>();
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.WEATHERS.toString());
            results = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Weather>) results;
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
