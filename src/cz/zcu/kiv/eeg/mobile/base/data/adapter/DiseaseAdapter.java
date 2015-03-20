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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Disease records in ListView.
 *
 * @author Petr Miko
 */
public class DiseaseAdapter extends AbstractAdapter<Disease> {
    /**
     * Constructor of disease array adapter.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      item collection
     */
    public DiseaseAdapter(Context context, int resourceId, List<Disease> items) {
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
        Disease record = getItem(position);
        if (record != null) {
            TextView diseaseName = (TextView) row.findViewById(R.id.row_disease_name);
            TextView diseaseDescription = (TextView) row.findViewById(R.id.row_disease_description);

            if (diseaseName != null) {
                diseaseName.setText(record.getName());
            }
            if (diseaseDescription != null) {
                diseaseDescription.setText(record.getDescription());
            }
        }
        return row;
    }
}
