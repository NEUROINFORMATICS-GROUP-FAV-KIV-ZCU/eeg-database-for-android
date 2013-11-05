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
package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;

/**
 * Fragment for displaying reservation details.
 *
 * @author Petr Miko
 */
public class ReservationDetailsFragment extends Fragment {

    public final static String TAG = ReservationDetailsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Reservation reservation = getArguments() != null ? (Reservation) getArguments().getParcelable("data") : null;
        boolean hasData = reservation != null && getArguments().getInt("index", -1) >= 0;
        if (hasData) {
            View view = inflater.inflate(R.layout.reser_details, container, false);
            initView(view, reservation);
            return view;
        } else {
            return inflater.inflate(R.layout.details_empty, container, false);
        }
    }

    /**
     * Initializes view elements with reservation data.
     *
     * @param view        view to be displayed
     * @param reservation reservation data
     */
    private void initView(View view, Reservation reservation) {

        //obtaining view elements
        TextView groupName = (TextView) view.findViewById(R.id.groupValue);
        TextView fromTime = (TextView) view.findViewById(R.id.fromValue);
        TextView toTime = (TextView) view.findViewById(R.id.toValue);
        TextView creatorName = (TextView) view.findViewById(R.id.creatorName);
        TextView creatorMail = (TextView) view.findViewById(R.id.creatorMail);

        //setting data
        groupName.setText(reservation.getResearchGroup());
        fromTime.setText(reservation.getFromTime().toString());
        toTime.setText(reservation.getToTime().toString());
        creatorName.setText(reservation.getCreatorName());
        creatorMail.setText(reservation.getEmail());
    }

    /**
     * Adds menu items to action bar.
     *
     * @param menu     menu to inflate into
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
