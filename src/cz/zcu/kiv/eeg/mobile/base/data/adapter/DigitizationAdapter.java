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
package cz.zcu.kiv.eeg.mobile.base.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Digitization records in ListView.
 *
 * @author Petr Miko
 */
public class DigitizationAdapter extends AbstractAdapter<Digitization> {
    /**
     * Constructor of disease array adapter.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      item collection
     */
    public DigitizationAdapter(Context context, int resourceId, List<Digitization> items) {
        super(context, resourceId, items);
    }

    /**
     * Creates row view using proper layout and data.
     *
     * @param position    row position, ie. position inside data collection
     * @param convertView view, where row should be displayed in
     * @param parent      view, where row should be displayed in
     * @return row view
     */
    protected View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Digitization record = getItem(position);
        if (record != null) {
            TextView filter = (TextView) row.findViewById(R.id.row_digitization_filter);
            TextView samplingRate = (TextView) row.findViewById(R.id.row_digitization_sampling_rate);
            TextView gain = (TextView) row.findViewById(R.id.row_digitization_gain);

            if (filter != null) {
                filter.setText(record.getFilter());
            }

            if (samplingRate != null) {
                samplingRate.setText(Float.toString(record.getSamplingRate()));
            }
            if (gain != null) {
                gain.setText(Float.toString(record.getGain()));
            }
        }
        return row;
    }
}
