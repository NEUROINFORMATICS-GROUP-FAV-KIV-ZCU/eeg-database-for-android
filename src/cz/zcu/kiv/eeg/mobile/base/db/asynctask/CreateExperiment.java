package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (Asynctask) for creating new Experiment on eeg base.
 *
 * @author Petr Miko
 */
public class CreateExperiment extends CommonService<Experiment, Void, Experiment> {

    private final static String TAG = CreateExperiment.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateExperiment(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where Experiment information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param experiments only one Experiment object is accepted
     * @return information about created experiment
     */
    @Override
    protected Experiment doInBackground(Experiment... experiments) {

        Experiment experiment = experiments[0];

        try {
            WaspDbSupport support = new WaspDbSupport();
            WaspHash hash = support.getOrCreateHash(HashConstants.EXPERIMENTS.toString());
            hash.put("hash" + experiment.hashCode(), experiment);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return experiment;
    }

    /**
     * Informs user whether experiment creation was successful or not.
     *
     * @param experiment returned experiment info if any
     */
    @Override
    protected void onPostExecute(Experiment experiment) {
        if (experiment != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_EXPERIMENT_KEY, experiment);
            Toast.makeText(activity, R.string.creation_experiment_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
