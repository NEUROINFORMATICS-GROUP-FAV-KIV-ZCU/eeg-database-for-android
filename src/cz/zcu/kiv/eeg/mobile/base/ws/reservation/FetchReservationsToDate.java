package cz.zcu.kiv.eeg.mobile.base.ws.reservation;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

import java.text.SimpleDateFormat;
import java.util.*;

import cz.zcu.kiv.eeg.mobile.base.data.Values;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.container.Reservation;
import cz.zcu.kiv.eeg.mobile.base.data.container.ReservationAdapter;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ReservationData;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ReservationDataList;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class FetchReservationsToDate extends CommonService<Integer, Void, List<ReservationData>> {

	private static final String TAG = FetchReservationsToDate.class.getSimpleName();

	private ReservationAdapter reservationAdapter;

	public FetchReservationsToDate(CommonActivity activity, ReservationAdapter reservationAdapter) {
		super(activity);
		this.reservationAdapter = reservationAdapter;
	}

	@Override
	protected List<ReservationData> doInBackground(Integer... params) {
		SharedPreferences credentials = getCredentials();
		String username = credentials.getString("username", null);
		String password = credentials.getString("password", null);
		String url = credentials.getString("url", null)  + Values.SERVICE_RESERVATION;

		if (params.length == 3) {
			url = url + params[0] + "-" + params[1] + "-" + params[2];
		} else {
			Log.e(TAG, "Invalid params count! Must be 3 in order of year, month, day");
			setState(ERROR, "Invalid params count! Must be 3 in order of year, month, day");
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
			ResponseEntity<ReservationDataList> response = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<Object>(requestHeaders), ReservationDataList.class);
			ReservationDataList body = response.getBody();

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
	protected void onPostExecute(List<ReservationData> resultList) {
		reservationAdapter.clear();
		if (resultList != null && !resultList.isEmpty())
			for (ReservationData res : resultList) {
				try {
					SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
					Date fromTime = sf.parse(res.getFromTime());
					Date toTime = sf.parse(res.getToTime());
					Reservation reservation = new Reservation(res.getReservationId(), res.getResearchGroup(), res.getResearchGroupId(), fromTime, toTime,
							res.getCreatorName(), res.getCreatorMailUsername() + "@" + res.getCreatorMailDomain(), res.getCanRemove());
					reservationAdapter.add(reservation);
				} catch (Exception e) {
					setState(ERROR, e);
					Log.e(TAG, e.getLocalizedMessage(), e);
				}
			}
	}
}
