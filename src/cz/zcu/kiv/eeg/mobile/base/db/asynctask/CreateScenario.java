package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.io.File;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (Asynctask) for creating new scenario on eeg base.
 *
 * @author Petr Miko
 */
public class CreateScenario extends CommonService<Scenario, Void, Scenario> {

    private final static String TAG = CreateScenario.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateScenario(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where scenario information is pushed to server in order to new scenario.
     * All heavy lifting is made here.
     *
     * @param scenarios only one scenario is accepted - scenario to be uploaded
     * @return scenario stored
     */
    @Override
    protected Scenario doInBackground(Scenario... scenarios) {


        Scenario scenario = scenarios[0];


            try {
                WaspDbSupport support = new WaspDbSupport();
                WaspHash hash = support.getOrCreateHash(HashConstants.SCENARIOS.toString());
                long fileLength = new File(scenario.getFilePath()).length();
                scenario.setFileLength(String.valueOf(fileLength));
                hash.put("hash" + scenario.hashCode(), scenario);

            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                setState(ERROR, e);
            } finally {
                setState(DONE);
            }
        return scenario;
    }

    /**
     * If scenario was created successfully, URI to scenario file will be displayed shortly.
     *
     * @param scenario created scenario if any
     */
    @Override
    protected void onPostExecute(Scenario scenario) {
        if (scenario != null) {

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_SCENARIO_KEY, scenario);
            activity.setResult(Activity.RESULT_OK, resultIntent);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.finish();

        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
