package cz.zcu.kiv.eeg.mobile.base.ws.reservation;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

import java.text.ParseException;
import java.util.Collections;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Constants;
import cz.zcu.kiv.eeg.mobile.base.data.container.Reservation;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ReservationData;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class CreateReservation extends CommonService<ReservationData, Void, Boolean> {

	private static final String TAG = CreateReservation.class.getSimpleName();

	private ReservationData data;

	public CreateReservation(CommonActivity context) {
		super(context);
	}

	@Override
	protected Boolean doInBackground(ReservationData... params) {

		data = params[0];

		// will be fixed properly in future
		if (data == null)
			return false;

		try {

			setState(RUNNING, R.string.working_ws_create);

			SharedPreferences credentials = getCredentials();
			String username = credentials.getString("username", null);
			String password = credentials.getString("password", null);
			String url = credentials.getString("url", null) + "/reservation/";

			HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAuthorization(authHeader);
			requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
			requestHeaders.setContentType(MediaType.APPLICATION_XML);
			HttpEntity<ReservationData> entity = new HttpEntity<ReservationData>(data, requestHeaders);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
			restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

			Log.d(TAG, url);
			 ResponseEntity<ReservationData> dataEntity = restTemplate.postForEntity(url, entity, ReservationData.class);
			 data = dataEntity.getBody();
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
			try {
				Intent resultIntent = new Intent();
				Reservation record = new Reservation(data.getReservationId(), data.getResearchGroup(), data.getResearchGroupId(), data.getFromTime(),
						data.getToTime(), data.getCreatorName(), data.getCreatorMailUsername() + "@"
								+ data.getCreatorMailDomain(),data.getCanRemove());
				resultIntent.putExtra(Constants.ADD_RECORD_KEY, record);
				Toast.makeText(activity, activity.getString(R.string.reser_created), Toast.LENGTH_SHORT).show();
				activity.setResult(Activity.RESULT_OK, resultIntent);
				activity.finish();
			} catch (ParseException e) {
				Log.e(TAG, e.getLocalizedMessage());
				setState(ERROR, e);
			}
		}
	}
}
