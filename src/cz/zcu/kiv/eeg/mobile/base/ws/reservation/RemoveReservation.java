package cz.zcu.kiv.eeg.mobile.base.ws.reservation;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

import java.text.SimpleDateFormat;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.AgendaActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.AgendaFragment;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ReservationData;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class RemoveReservation extends CommonService<ReservationData, Void, Boolean> {

	private static final String TAG = RemoveReservation.class.getSimpleName();
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	private ReservationData data;

	public RemoveReservation(CommonActivity context) {
		super(context);
	}

	@Override
	protected Boolean doInBackground(ReservationData... params) {

		data = params[0];

		// will be fixed properly in future
		if (data == null)
			return false;

		try {

			setState(RUNNING, R.string.working_ws_remove);

			SharedPreferences credentials = getCredentials();
			String username = credentials.getString("username", null);
			String password = credentials.getString("password", null);
			String url = credentials.getString("url", null) + "reservation/" + data.getReservationId();

			HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAuthorization(authHeader);
			HttpEntity<ReservationData> entity = new HttpEntity<ReservationData>(requestHeaders);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

			Log.d(TAG, url + "\n" + entity);
			restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
			setState(ERROR, e);
		} finally {
			setState(DONE);
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			if (activity instanceof AgendaActivity) {
				AgendaFragment fragment = (AgendaFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_agenda);
				if(fragment != null){
					fragment.updateData();
				Toast.makeText(activity, activity.getString(R.string.reser_removed), Toast.LENGTH_SHORT).show();
				}else{
					Log.e(TAG, "Agenda fragment not found!");
					Toast.makeText(activity, activity.getString(R.string.reser_removed_update), Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}
