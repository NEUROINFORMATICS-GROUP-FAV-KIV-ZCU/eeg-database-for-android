package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;

/**
 * Common service (AsyncTask) for creating reservation on eeg base.
 *
 * @author Petr Miko
 */
public class CreateReservation extends CommonService<Reservation, Void, Reservation> {

    private static final String TAG = CreateReservation.class.getSimpleName();

    /**
     * Constructor.
     *
     * @param context parent activity
     */
    public CreateReservation(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where reservation information is pushed to server in order to create reservation record.
     * All heavy lifting is made here.
     *
     * @param params only one reservation instance is allowed here - reservation to be created
     * @return object of created reservation if any
     */
    @Override
    protected Reservation doInBackground(Reservation... params) {

        Reservation data = params[0];
        try {
            WaspDbSupport support = new WaspDbSupport();
            WaspHash hash = support.getOrCreateHash(HashConstants.RESERVATIONS.toString());
            hash.put("hash" + data.hashCode(), data);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return data;
    }

    /**
     * If reservation was created
     *
     * @param reservation created reservation if any
     */
    @Override
    protected void onPostExecute(Reservation reservation) {

        if (reservation != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_RESERVATION_KEY, reservation);
            Toast.makeText(activity, activity.getString(R.string.reser_created), Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        }
    }
}
