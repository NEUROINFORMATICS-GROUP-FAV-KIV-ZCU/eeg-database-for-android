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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Hardware;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Hardware records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class HardwareAdapter extends ArrayAdapter<Hardware> {

    private final Context context;
    private final int resourceId;

    /**
     * Adapter constructor.
     *
     * @param context    context
     * @param resourceId hardware layout identifier
     * @param items      hardware item collection
     */
    public HardwareAdapter(Context context, int resourceId, List<Hardware> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    /**
     * Getter of row view.
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Getter of row view in drop down element (spinner like).
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Getter of row view.
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    private View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Hardware record = getItem(position);
        if (record != null) {
            TextView hwId = (TextView) row.findViewById(R.id.row_hardware_id);
            TextView hwTitle = (TextView) row.findViewById(R.id.row_hardware_title);
            TextView hwType = (TextView) row.findViewById(R.id.row_hardware_type);

            if (hwId != null) {
                hwId.setText(record.getHardwareId());
            }
            if (hwTitle != null) {
                hwTitle.setText(record.getTitle());
            }
            if (hwType != null) {
                hwType.setText(record.getType());
            }
        }
        return row;
    }
}
