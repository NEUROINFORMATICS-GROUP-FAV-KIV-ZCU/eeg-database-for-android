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
package cz.zcu.kiv.eeg.mobile.base.ui.filechooser;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.FileAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for browsing devices file system. On file select activity ends and returns path to selected file.
 *
 * @author Petr Miko
 */
public class FileChooserActivity extends ListActivity {

    private final static String TAG = FileChooserActivity.class.getSimpleName();
    private final String ROOT = "/";
    private List<FileInfo> files = new ArrayList<FileInfo>();
    private TextView currentUrlView;
    private static String currentPath = "/";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fchooser_list);
        currentUrlView = (TextView) findViewById(R.id.url);

        if(savedInstanceState != null)
            currentPath = savedInstanceState.getString("path");

        loadDir(currentPath);
    }

    /**
     * Loads directory content on specified path.
     *
     * @param dirPath path to directory
     */
    private void loadDir(String dirPath) {

        currentPath = dirPath;

        Log.d(TAG, "Loading path " + dirPath);
        File f = new File(dirPath);
        File[] files = f.listFiles();
        currentUrlView.setText(f.getAbsolutePath());
        this.files.clear();

        for (File file : files) {
            //application does not have permission to list
            if (file.isDirectory() && file.listFiles() == null)
                continue;
            //application has permission to access file
            if (file.length() != 0)
                this.files.add(new FileInfo(file, file.getName()));
        }

        FileAdapter fileList = new FileAdapter(this, R.layout.fchooser_list_row, this.files);
        fileList.sort(FileInfo.getFileComparator());

        //adding references to previous directory and to root
        if (!dirPath.equals(ROOT)) {
            this.files.add(0, new FileInfo(f.getParentFile(), ".."));
            this.files.add(0, new FileInfo(new File(ROOT), ROOT));
        }
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File file = this.files.get(position);
        Log.d(TAG, "Clicked on file: " + file.getName());
        if (file.isDirectory())
            loadDir(file.getAbsolutePath());
        else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.FILE_PATH, file.getAbsolutePath());
            resultIntent.putExtra(Values.FILE_LENGTH, file.length());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", currentPath);
    }
}
