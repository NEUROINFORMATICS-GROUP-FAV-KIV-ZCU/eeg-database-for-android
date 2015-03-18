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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Weather records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class WeatherAdapter extends AbstractAdapter<Weather> {

    /**
     * Adapter constructor.
     *
     * @param context    context
     * @param resourceId weather layout identifier
     * @param items      weather item collection
     */
    public WeatherAdapter(Context context, int resourceId, List<Weather> items) {
        super(context, resourceId, items);
    }

    /**
     * Getter of row view.
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    protected View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Weather record = getItem(position);
        if (record != null) {
            TextView swTitle = (TextView) row.findViewById(R.id.row_weather_title);
            TextView swDescription = (TextView) row.findViewById(R.id.row_weather_description);

            if (swTitle != null) {
                swTitle.setText(record.getTitle());
            }
            if (swDescription != null) {
                swDescription.setText(record.getDescription());
            }
        }
        return row;
    }
}
