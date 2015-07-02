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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.localdb.CBDatabase;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.Keys;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateDigitization;

/**
 * Activity for creating new digitization record.
 *
 * @author Petr Miko
 */
public class DigitizationAddActivity extends SaveDiscardActivity {

    private CBDatabase db;
    private String default_researchGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_digitization_add);
        default_researchGroupId = getIntent().getStringExtra("groupId");
        initView();
    }

    /**
     * Initializes view elements.
     */
    private void initView() {

        EditText filter = (EditText) findViewById(R.id.experiment_digitization_filter_add);

        TextView filterCount = (TextView) findViewById(R.id.experiment_digitization_filter_add_count);
        filter.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_artifact), filterCount));
    }

    /**
     * Reads data from fields, if valid proceeds with creating new record on server.
     */
    @Override
    protected void save() {

        Digitization record;
        if ((record = getValidRecord()) != null) {
//===================================================================Database Code==================================================//
            db = new CBDatabase(Keys.DB_NAME, DigitizationAddActivity.this);
            // create an object that contains data for a document
            Map<String, Object> docContent = new HashMap<String, Object>();
            docContent.put("type", "Digitization");
            docContent.put("filter",record.getFilter());
            docContent.put("gain", record.getGain());
            docContent.put("sampling_rate", record.getSamplingRate());
            docContent.put("def_group_id", default_researchGroupId); //create an attribute to make a relationship (Belongs to) with "ResearchGroup" entity

            String digitizationID = null;
            try {
                // Create a new document
                digitizationID = db.create(docContent);

                assert(digitizationID != null);
                Intent resultIntent = new Intent();
                record.setDigitizationId(digitizationID);
                resultIntent.putExtra(Values.ADD_DIGITIZATION_KEY, record);
                Toast.makeText(DigitizationAddActivity.this, R.string.creation_ok, Toast.LENGTH_SHORT).show();
                DigitizationAddActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                DigitizationAddActivity.this.finish();
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(DigitizationAddActivity.this, R.string.creation_failed, Toast.LENGTH_SHORT).show();
            }
// ================================================================================================================================//


//            if (ConnectionUtils.isOnline(this)) {
//                new CreateDigitization(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, record);
//            } else
//                showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * Returns valid record or null, if input values are not valid.
     *
     * @return valid record
     */
    private Digitization getValidRecord() {

        EditText filter = (EditText) findViewById(R.id.experiment_digitization_filter_add);
        EditText samplingRate = (EditText) findViewById(R.id.experiment_digitization_sampling_rate_add);
        EditText gain = (EditText) findViewById(R.id.experiment_digitization_gain_add);

        StringBuilder error = new StringBuilder();

        //validations
        if (ValidationUtils.isEmpty(filter.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_digitization_filter)).append(")").append('\n');
        if (ValidationUtils.isEmpty(samplingRate.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_digitization_sampling_rate)).append(")").append('\n');
        if (ValidationUtils.isEmpty(gain.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_digitization_gain)).append(")").append('\n');

        //if no error, run service
        if (error.toString().isEmpty()) {
            Digitization record = new Digitization();
            record.setFilter(filter.getText().toString());
            record.setGain(gain.getText().toString());
            record.setSamplingRate(samplingRate.getText().toString());

            return record;
        } else {
            showAlert(error.toString());
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void discard() {
        finish();
    }
}
