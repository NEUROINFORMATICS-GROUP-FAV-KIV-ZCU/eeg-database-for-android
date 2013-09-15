package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ReservationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ReservationList;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.TimeContainer;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.ReservationDetailsFragment;
import org.springframework.http.*;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Service (AsyncTask) for fetching reservations created to specified day.
 *
 * @author Petr Miko
 */
public class FetchReservationsToDate extends CommonService<TimeContainer, Void, List<Reservation>> {

    private static final String TAG = FetchReservationsToDate.class.getSimpleName();
    private static final int MESSAGE = R.string.working_ws_msg;
    private ReservationAdapter reservationAdapter;

    /**
     * Constructor.
     *
     * @param activity           parent activity
     * @param reservationAdapter adapter into which should be stored fetched reservations
     */
    public FetchReservationsToDate(CommonActivity activity, ReservationAdapter reservationAdapter) {
        super(activity, MESSAGE);
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
        SharedPreferences credentials = getPreferences();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_RESERVATION;

        if (params.length == 1) {
            TimeContainer time = params[0];
            url = url + time.getDay() + "-" + time.getMonth() + "-" + time.getYear();
        } else {
            Log.e(TAG, "Invalid params count! There must be one TimeContainer instance");
            onServiceError(new Throwable("Invalid params count! There must be one TimeContainer instance"));
            return Collections.emptyList();
        }

        onServiceStart();

        // Populate the HTTP Basic Authentication header with the username and
        // password
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        RestTemplate restTemplate = createRestClientInstance();
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        try {
            // Make the network request
            Log.d(TAG, url);
            ResponseEntity<ReservationList> response = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<Object>(requestHeaders), ReservationList.class);
            ReservationList body = response.getBody();

            if (body != null) {
                return body.getReservations();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            onServiceError(e);
        } finally {
            onServiceDone();
        }
        return Collections.emptyList();
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
                    onServiceError(e);
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }
            }
        }

        FragmentManager fm = activity.getSupportFragmentManager();

        ReservationDetailsFragment details = new ReservationDetailsFragment();
        ReservationDetailsFragment frag = (ReservationDetailsFragment) fm.findFragmentByTag(ReservationDetailsFragment.TAG);
        if (frag != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.details, details, ReservationDetailsFragment.TAG);
            ft.commit();
        }
    }
}
