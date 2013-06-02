package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (Asynctask) for creating new Digitization on eeg base.
 *
 * @author Petr Miko
 */
public class CreateDigitization extends CommonService<Digitization, Void, Digitization> {

    private final static String TAG = CreateDigitization.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateDigitization(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where digitization information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param digitizations only one Digitization object is accepted
     * @return information about created digitization
     */
    @Override
    protected Digitization doInBackground(Digitization... digitizations) {


        Digitization digitization = digitizations[0];

        try {
            WaspDbSupport support = new WaspDbSupport();
            WaspHash hash = support.getOrCreateHash(HashConstants.DIGITISATION.toString());
            hash.put("hash" + digitization.hashCode(), digitization);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return digitization;
    }

    /**
     * Informs user whether digitization creation was successful or not.
     *
     * @param digitization returned digitization info if any
     */
    @Override
    protected void onPostExecute(Digitization digitization) {
        if (digitization != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_DIGITIZATION_KEY, digitization);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
