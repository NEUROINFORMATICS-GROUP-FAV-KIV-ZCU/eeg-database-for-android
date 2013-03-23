package cz.zcu.kiv.eeg.mobile.base.ws.reservation;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.ReservationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ReservationList;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.TimeContainer;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.ReservationDetailsFragment;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

@SuppressLint("SimpleDateFormat")
public class FetchReservationsToDate extends CommonService<TimeContainer, Void, List<Reservation>> {

    private static final String TAG = FetchReservationsToDate.class.getSimpleName();
    private ReservationAdapter reservationAdapter;

    public FetchReservationsToDate(CommonActivity activity, ReservationAdapter reservationAdapter) {
        super(activity);
        this.reservationAdapter = reservationAdapter;
    }

    @Override
    protected List<Reservation> doInBackground(TimeContainer... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_RESERVATION;

        if (params.length == 1) {
            TimeContainer time = params[0];
            url = url + time.getDay() + "-" + time.getMonth() + "-" + time.getYear();
        } else {
            Log.e(TAG, "Invalid params count! There must be one TimeContainer instance");
            setState(ERROR, "Invalid params count! There must be one TimeContainer instance");
            return Collections.emptyList();
        }

        setState(RUNNING, R.string.working_ws_msg);

        // Populate the HTTP Basic Authentication header with the username and
        // password
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
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
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return Collections.emptyList();
    }

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
