package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ReservationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.TimeContainer;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.ReservationDetailsFragment;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Service (AsyncTask) for fetching reservations created to specified day.
 *
 * @author Petr Miko
 */
public class FetchReservationsToDate extends CommonService<TimeContainer, Void, List<Reservation>> {

    private static final String TAG = FetchReservationsToDate.class.getSimpleName();
    private ReservationAdapter reservationAdapter;

    /**
     * Constructor.
     *
     * @param activity           parent activity
     * @param reservationAdapter adapter into which should be stored fetched reservations
     */
    public FetchReservationsToDate(CommonActivity activity, ReservationAdapter reservationAdapter) {
        super(activity);
        this.reservationAdapter = reservationAdapter;
    }

    /**
     * Method, where all reservations to specified date are read from server.
     * All heavy lifting is made here.
     *
     * @param params only one TimeContainer parameter is allowed here - specifies day, month and year
     * @return list of fetched reservations
     */
    @Override
    protected List<Reservation> doInBackground(TimeContainer... params) {

        if (params.length == 1) {
            TimeContainer time = params[0];
        } else {
            Log.e(TAG, "Invalid params count! There must be one TimeContainer instance");
            setState(ERROR, "Invalid params count! There must be one TimeContainer instance");
            return Collections.emptyList();
        }

        setState(RUNNING, R.string.working_ws_msg);

      List<?> results = new ArrayList<Object>();
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.RESERVATIONS.toString());
            results = hash.getAllValues();

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Reservation>) results;
    }

    /**
     * Clears adapter of current data and fills it with fetched reservations.
     * In process it clears details fragment, so it could not display information about no longer existing reservation.
     *
     * @param resultList fetched reservations
     */
    @Override
    protected void onPostExecute(List<Reservation> resultList) {
        reservationAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            for (Reservation reservation : resultList) {
                try {
                    reservationAdapter.add(reservation);
                } catch (Exception e) {
                    setState(ERROR, e);
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }
            }
        }

        FragmentManager fm = activity.getFragmentManager();

        ReservationDetailsFragment details = new ReservationDetailsFragment();
        ReservationDetailsFragment frag = (ReservationDetailsFragment) fm.findFragmentByTag(ReservationDetailsFragment.TAG);
        if (frag != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.details, details, ReservationDetailsFragment.TAG);
            ft.commit();
        }
    }
}
