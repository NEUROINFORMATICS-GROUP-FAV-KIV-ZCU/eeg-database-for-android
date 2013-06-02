package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (Asynctask) for creating new Weather on eeg base.
 *
 * @author Petr Miko
 */
public class CreateWeather extends CommonService<Weather, Void, Weather> {

    private final static String TAG = CreateWeather.class.getSimpleName();
    private int researchGroupId;

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateWeather(CommonActivity context, int researchGroupId) {
        super(context);
        this.researchGroupId = researchGroupId;
    }

    /**
     * Method, where Weather information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param weathers only one Weather object is accepted
     * @return information about created weather
     */
    @Override
    protected Weather doInBackground(Weather... weathers) {
        Weather weather = weathers[0];
        try {
            WaspDbSupport support = new WaspDbSupport();
            WaspHash hash = support.getOrCreateHash(HashConstants.WEATHERS.toString());
            hash.put("hash" + weather.hashCode(), weather);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return weather;
    }

    /**
     * Informs user whether weather creation was successful or not.
     *
     * @param weather returned weather info if any
     */
    @Override
    protected void onPostExecute(Weather weather) {
        if (weather != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_WEATHER_KEY, weather);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
