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
package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeFixAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeTypeAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeLocation;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeType;
import cz.zcu.kiv.eeg.mobile.base.localdb.CBDatabase;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.Keys;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateElectrodeLocation;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchElectrodeFixes;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchElectrodeTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for creating new electrode location record.
 *
 * @author Petr Miko
 */
public class ElectrodeLocationAddActivity extends SaveDiscardActivity {

    private static ElectrodeFixAdapter electrodeFixAdapter;
    private static ElectrodeTypeAdapter electrodeTypeAdapter;
    private CBDatabase db;
    private String default_researchGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_electrode_add);
        default_researchGroupId = getIntent().getStringExtra("groupId");
        initView();
        updateData();
    }

    /**
     * Initializes spinners and character counter for description field.
     */
    private void initView() {

        Spinner fixes = (Spinner) findViewById(R.id.electrode_add_fix);
        Spinner types = (Spinner) findViewById(R.id.electrode_add_type);

        fixes.setAdapter(getElectrodeFixAdapter());
        types.setAdapter(getElectrodeTypeAdapter());

        TextView description = (TextView) findViewById(R.id.electrode_add_description);
        TextView descriptionCount = (TextView) findViewById(R.id.electrode_add_description_count);
        description.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_description_chars), descriptionCount));

        ImageButton addFix = (ImageButton) findViewById(R.id.electrode_add_fix_new);
        addFix.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.electrode_add_fix_new:
                Intent intent = new Intent();
                intent.setClass(this, ElectrodeFixAddActivity.class);
                intent.putExtra("groupId", default_researchGroupId);
                startActivityForResult(intent, Values.ADD_ELECTRODE_FIX_FLAG);
                break;
            default:
                super.onClick(v);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!isWorking()) {
                    updateElectrodeFixes();
                    updateElectrodeTypes();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Values.ADD_ELECTRODE_FIX_FLAG && resultCode == Activity.RESULT_OK) {
            ElectrodeFix fix = (ElectrodeFix) data.getExtras().get(Values.ADD_ELECTRODE_FIX_KEY);
            electrodeFixAdapter.add(fix);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Fetches data from server, if not already loaded or currently fetching.
     */
    private void updateData() {
        if (!isWorking()) {
            if (getElectrodeFixAdapter().isEmpty())
                updateElectrodeFixes();
            if (getElectrodeTypeAdapter().isEmpty())
                updateElectrodeTypes();
        }
    }

    /**
     * Reads data from fields, if valid proceeds with creating new record on server.
     */
    @Override
    protected void save() {
        ElectrodeLocation record;
        if ((record = getValidRecord()) != null) {
//===================================================================Database Code==================================================//
            db = new CBDatabase(Keys.DB_NAME, ElectrodeLocationAddActivity.this);
            // create an object that contains data for a document
            Map<String, Object> docContent = new HashMap<String, Object>();

            docContent.put("type", "ElectrodeLocation");
            docContent.put("title",record.getTitle());
            docContent.put("abbr",record.getAbbr());
            docContent.put("description", record.getDescription());
            docContent.put("default_number", record.getDefaultNumber());
            docContent.put("electrode_fix_id", record.getElectrodeFix().getId());
            docContent.put("electrode_type_id", record.getElectrodeType().getId());
            docContent.put("def_group_id", default_researchGroupId);

            String electrodeLocationID = null;
            try {
                // Create a new document
                electrodeLocationID = db.create(docContent);
                assert(electrodeLocationID != null);
                Intent resultIntent = new Intent();
                record.setId(electrodeLocationID);
                resultIntent.putExtra(Values.ADD_ELECTRODE_LOCATION_KEY, record);
                Toast.makeText(ElectrodeLocationAddActivity.this, R.string.creation_ok, Toast.LENGTH_SHORT).show();
                ElectrodeLocationAddActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                ElectrodeLocationAddActivity.this.finish();
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ElectrodeLocationAddActivity.this, R.string.creation_failed, Toast.LENGTH_SHORT).show();
            }
// ================================================================================================================================//


//            if (ConnectionUtils.isOnline(this)) {
//                new CreateElectrodeLocation(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, record);
//            } else
//                showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void discard() {
        finish();
    }

    /**
     * Method for fetching electrode fixes from server.
     * If not online, shows error dialog.
     */
    private void updateElectrodeFixes() {
//        if (ConnectionUtils.isOnline(this))
//            new FetchElectrodeFixes(this, getElectrodeFixAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
//        else
//            showAlert(getString(R.string.error_offline));
        CommonActivity activity = (CommonActivity) this;
        db = new CBDatabase(Keys.DB_NAME, activity);
        db.createElectrodeFixesView("fetchAllElectrodeFixRecordsView", "ElectrodeFix", getElectrodeFixAdapter());

    }

    /**
     * Method for fetching electrode types from server.
     * If not online, shows error dialog.
     */
    private void updateElectrodeTypes() {
//        if (ConnectionUtils.isOnline(this))
//            new FetchElectrodeTypes(this, getElectrodeTypeAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
//        else
//            showAlert(getString(R.string.error_offline));
        CommonActivity activity = (CommonActivity) this;
        db = new CBDatabase(Keys.DB_NAME, activity);
        db.createElectrodeTypesView("fetchAllElectrodeTypeRecordsView", "ElectrodeType", getElectrodeTypeAdapter());
    }

    /**
     * Getter of electrode fix adapter. If null, creates new.
     *
     * @return electrode fix adapter
     */
    private ElectrodeFixAdapter getElectrodeFixAdapter() {
        if (electrodeFixAdapter == null)
            electrodeFixAdapter = new ElectrodeFixAdapter(this, R.layout.base_electrode_simple_row, new ArrayList<ElectrodeFix>());
        return electrodeFixAdapter;
    }

    /**
     * Getter of electrode type adapter. If null, creates new.
     *
     * @return electrode type adapter
     */
    private ElectrodeTypeAdapter getElectrodeTypeAdapter() {
        if (electrodeTypeAdapter == null)
            electrodeTypeAdapter = new ElectrodeTypeAdapter(this, R.layout.base_electrode_simple_row, new ArrayList<ElectrodeType>());
        return electrodeTypeAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.exp_add_menu, menu);
        return true;
    }

    /**
     * Returns valid record or null, if input values are not valid.
     *
     * @return valid record
     */
    private ElectrodeLocation getValidRecord() {

        EditText title = (EditText) findViewById(R.id.electrode_add_title);
        EditText abbr = (EditText) findViewById(R.id.electrode_add_abbr);
        EditText description = (EditText) findViewById(R.id.electrode_add_description);
        Spinner type = (Spinner) findViewById(R.id.electrode_add_type);
        Spinner fix = (Spinner) findViewById(R.id.electrode_add_fix);

        StringBuilder error = new StringBuilder();

        ElectrodeFix fixData = (ElectrodeFix) fix.getSelectedItem();
        ElectrodeType typeData = (ElectrodeType) type.getSelectedItem();

        //validations
        if (ValidationUtils.isEmpty(title.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.dialog_title)).append(")").append('\n');
        if (ValidationUtils.isEmpty(description.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.dialog_description)).append(")").append('\n');
        if (ValidationUtils.isEmpty(abbr.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.dialog_abbr)).append(")").append('\n');
        if (fixData == null)
            error.append(getString(R.string.error_no_fix_selected)).append('\n');
        if (typeData == null)
            error.append(getString(R.string.error_no_type_selected)).append('\n');

        //if no error, run service
        if (error.toString().isEmpty()) {
            ElectrodeLocation record = new ElectrodeLocation();
            record.setTitle(title.getText().toString());
            record.setAbbr(abbr.getText().toString());
            record.setDescription(description.getText().toString());

            record.setElectrodeFix(fixData);
            record.setElectrodeType(typeData);

            return record;
        } else {
            showAlert(error.toString());
        }

        return null;
    }
}
