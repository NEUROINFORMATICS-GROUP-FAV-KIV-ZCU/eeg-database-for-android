package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (Asynctask) for creating new Electrode Fix on eeg base.
 *
 * @author Petr Miko
 */
public class CreateElectrodeFix extends CommonService<ElectrodeFix, Void, ElectrodeFix> {

    private final static String TAG = CreateElectrodeFix.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateElectrodeFix(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where electrode fix information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param electrodeFixes only one ElectrodeFix object is accepted
     * @return information about created electrode fix
     */
    @Override
    protected ElectrodeFix doInBackground(ElectrodeFix... electrodeFixes) {


        ElectrodeFix fix = electrodeFixes[0];

        try {
            WaspDbSupport support = new WaspDbSupport();
            WaspHash hash = support.getOrCreateHash(HashConstants.ELECTRODES_FIX.toString());
            hash.put("hash" + fix.hashCode(), fix);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return fix;
    }

    /**
     * Informs user whether ElectrodeFix creation was successful or not.
     *
     * @param fix returned electrode fix info if any
     */
    @Override
    protected void onPostExecute(ElectrodeFix fix) {
        if (fix != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_ELECTRODE_FIX_KEY, fix);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
