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
import cz.zcu.kiv.eeg.mobile.base.data.container.FileInfo;

import java.util.List;

/**
 * Adapter for displaying file content in list view.
 *
 * @author Petr Miko
 */
public class FileAdapter extends ArrayAdapter<FileInfo> {

    private final static String TAG = ReservationAdapter.class.getSimpleName();
    private final Context context;
    private final int resourceId;

    /**
     * Adapter constructor for File information.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      file information collection
     */
    public FileAdapter(Context context, int resourceId, List<FileInfo> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    /**
     * Getter of row view.
     *
     * @param position    position in data collection
     * @param convertView view in which should be row dispplayed
     * @param parent      view parent
     * @return row view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        FileInfo record = getItem(position);
        if (record != null) {
            TextView topText = (TextView) row.findViewById(R.id.fileName);
            TextView additionalText = (TextView) row.findViewById(R.id.fileSize);
            if (topText != null) {
                topText.setText(record.getName());
                if (record.isDirectory()) {
                    topText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dir, 0, 0, 0);
                } else {
                    topText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file, 0, 0, 0);
                }
            }
            if (additionalText != null) {
                if (record.isDirectory()) {
                    additionalText.setText("");
                } else {
                    additionalText.setText(record.getFileSize());
                }
            }
        }
        return row;
    }
}

