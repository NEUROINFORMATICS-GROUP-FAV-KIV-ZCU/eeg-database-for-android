/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.ui.NavigationActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.ReservationFragment;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Service (AsyncTask) for removing existing reservation from eeg base.
 * After removing record it forces reservation fragment to update its content.
 *
 * @author Petr Miko
 */
public class RemoveReservation extends CommonService<Reservation, Void, Boolean> {

    private static final String TAG = RemoveReservation.class.getSimpleName();
    private int fragmentId;

    /**
     * Constructor.
     *
     * @param context    parent activity
     * @param fragmentId identifier of reservation fragment (vital for refreshing)
     */
    public RemoveReservation(CommonActivity context, int fragmentId) {
        super(context);
        this.fragmentId = fragmentId;
    }

    /**
     * Method, where reservation information is pushed to server in order to remove it.
     * All heavy lifting is made here.
     *
     * @param params only one Reservation object is accepted
     * @return true if reservation is removed
     */
    @Override
    protected Boolean doInBackground(Reservation... params) {

        Reservation data = params[0];

        //nothing to remove
        if (data == null)
            return false;

        try {

            setState(RUNNING, R.string.working_ws_remove);

            SharedPreferences credentials = getCredentials();
            String username = credentials.getString("username", null);
            String password = credentials.getString("password", null);
            String url = credentials.getString("url", null) + Values.SERVICE_RESERVATION + data.getReservationId();

            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            HttpEntity<Reservation> entity = new HttpEntity<Reservation>(requestHeaders);

            SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate(factory);
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

    /**
     * If reservation was removed, attempt to update reservation fragment or at least inform user, that he should do that manually.
     *
     * @param success true if reservation is removed
     */
    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            if (activity instanceof NavigationActivity) {

                FragmentManager fm = activity.getFragmentManager();

                ReservationFragment fragment = (ReservationFragment) fm.findFragmentByTag(ReservationFragment.TAG);
                if (fragment == null)
                    fragment = (ReservationFragment) fm.findFragmentById(fragmentId);
                if (fragment != null) {
                    fragment.updateData();
                    Toast.makeText(activity, activity.getString(R.string.reser_removed), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Agenda fragment not found!");
                    Toast.makeText(activity, activity.getString(R.string.reser_removed_update), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
