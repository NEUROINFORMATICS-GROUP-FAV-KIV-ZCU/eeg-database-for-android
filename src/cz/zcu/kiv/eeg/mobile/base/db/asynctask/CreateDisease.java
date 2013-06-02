package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (Asynctask) for creating new Disease on eeg base.
 *
 * @author Petr Miko
 */
public class CreateDisease extends CommonService<Disease, Void, Disease> {

    private final static String TAG = CreateDisease.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateDisease(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where Disease information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param diseases only one Disease object is accepted
     * @return information about created disease
     */
    @Override
    protected Disease doInBackground(Disease... diseases) {

        Disease disease = diseases[0];

        try {
            WaspDbSupport support = new WaspDbSupport();
            WaspHash hash = support.getOrCreateHash(HashConstants.DISEASES.toString());
            hash.put("hash" + disease.hashCode(), disease);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return disease;
    }

    /**
     * Informs user whether disease creation was successful or not.
     *
     * @param disease returned disease info if any
     */
    @Override
    protected void onPostExecute(Disease disease) {
        if (disease != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_DISEASE_KEY, disease);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
