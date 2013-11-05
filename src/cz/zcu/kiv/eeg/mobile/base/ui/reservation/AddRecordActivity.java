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

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.TimeContainer;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateReservation;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchResearchGroups;

import java.util.ArrayList;

/**
 * Activity for creating new Reservation.
 *
 * @author Petr Miko
 */
@SuppressLint("SimpleDateFormat")
public class AddRecordActivity extends SaveDiscardActivity {

    private static final String TAG = AddRecordActivity.class.getSimpleName();
    private final OnTimeSetListener fromListener = new OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            fromTime.setHour(hourOfDay);
            fromTime.setMinute(minute);

            TextView fromField = (TextView) findViewById(R.id.fromField);
            fromField.setText(fromTime.toTimeString());

        }
    };
    private final OnTimeSetListener toListener = new OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            toTime.setHour(hourOfDay);
            toTime.setMinute(minute);

            TextView toField = (TextView) findViewById(R.id.toField);
            toField.setText(toTime.toTimeString());

        }
    };
    private TimeContainer fromTime, toTime;
    private ResearchGroupAdapter researchGroupAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Add new record activity loaded");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reser_add);

        Bundle b = getIntent().getExtras();
        TimeContainer originalDate = b.getParcelable("time");

        Time time = new Time();
        time.setToNow();

        fromTime = new TimeContainer(originalDate);
        fromTime.setHour(time.hour);
        fromTime.setMinute(time.minute);

        toTime = new TimeContainer(originalDate);
        toTime.setHour(time.hour);
        toTime.setMinute(time.minute);

        initFields();
        updateData();
    }

    /**
     * Sets default time values into from and to text fields. Also set adapter for research groups spinner.
     */
    private void initFields() {
        TextView fromField = (TextView) findViewById(R.id.fromField);
        TextView toField = (TextView) findViewById(R.id.toField);

        fromField.setText(fromTime.toTimeString());
        toField.setText(toTime.toTimeString());

        researchGroupAdapter = new ResearchGroupAdapter(this, R.layout.base_row_simple, new ArrayList<ResearchGroup>());
        Spinner groupList = (Spinner) findViewById(R.id.groupList);
        groupList.setAdapter(researchGroupAdapter);
    }

    /**
     * If online, fetches research groups into research groups adapter.
     */
    private void updateData() {
        if (ConnectionUtils.isOnline(this)) {
            new FetchResearchGroups(this, researchGroupAdapter, Values.SERVICE_QUALIFIER_MINE).execute();
        } else {
            showAlert(getString(R.string.error_offline), true);
        }
    }

    /**
     * On fromTime button click event shows TimePickerDialog.
     *
     * @param v fromTime button
     */
    public void fromTimeClick(View v) {
        TimePickerDialog fromDialog = new TimePickerDialog(this, fromListener, fromTime.getHour(), fromTime.getMinute(), true);
        fromDialog.show();
    }

    /**
     * On toTime button click event shows TimePickerDialog.
     *
     * @param v toTime button
     */
    public void toTimeClick(View v) {
        TimePickerDialog toDialog = new TimePickerDialog(this, toListener, toTime.getHour(), toTime.getMinute(), true);
        toDialog.show();
    }

    /**
     * {@inheritDoc}
     */
    public void save() {

        if (ConnectionUtils.isOnline(this)) {

            if (Time.compare(fromTime.getTime(), toTime.getTime()) >= 0) {
                Toast.makeText(this, R.string.error_date_comparison, Toast.LENGTH_SHORT).show();
                return;
            }

            ResearchGroup group = (ResearchGroup) ((Spinner) findViewById(R.id.groupList)).getSelectedItem();

            if (group == null) {
                Toast.makeText(this, R.string.error_no_group_selected, Toast.LENGTH_SHORT).show();
                return;
            }

            Reservation record = new Reservation();

            record.setResearchGroupId(group.getGroupId());
            record.setResearchGroup(group.getGroupName());
            record.setFromTime(fromTime);
            record.setToTime(toTime);
            record.setCanRemove(true);

            new CreateReservation(this).execute(record);
        } else {
            showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void discard() {
        finish();
    }
}
