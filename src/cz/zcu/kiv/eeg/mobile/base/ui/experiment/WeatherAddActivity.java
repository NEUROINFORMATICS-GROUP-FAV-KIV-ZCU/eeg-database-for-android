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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.localdb.CBDatabase;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.Keys;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateWeather;

/**
 * Activity for creating new weather record.
 *
 * @author Petr Miko
 */
public class WeatherAddActivity extends SaveDiscardActivity {

//    private int researchGroupId;
    private String researchGroupId;
    private CBDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_experiment_add_weather);
//        researchGroupId = getIntent().getIntExtra("groupId", -1);
        researchGroupId = getIntent().getStringExtra("groupId");

        if(researchGroupId == null || researchGroupId == ""){
            showAlert(getString(R.string.error_no_group_selected), true);
        }else{
            initView();
        }
    }

    /**
     * Initializes view elements.
     */
    private void initView() {
        final EditText title = (EditText) findViewById(R.id.experiment_add_weather_title);
        final EditText description = (EditText) findViewById(R.id.experiment_add_weather_description);
        TextView titleCount = (TextView) findViewById(R.id.experiment_add_weather_title_count);
        TextView descriptionCount = (TextView) findViewById(R.id.experiment_add_weather_description_count);
        description.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_weather), descriptionCount));
        title.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_weather), titleCount));
    }

    /**
     * Reads data from fields, if valid proceeds with creating new record on server.
     */
    @Override
    protected void save() {

        Weather record;
        if ((record = getValidRecord()) != null) {
//===================================================================Database Code==================================================//
            db = new CBDatabase(Keys.DB_NAME, WeatherAddActivity.this);
            // create an object that contains data for a document
            Map<String, Object> docContent = new HashMap<String, Object>();
            docContent.put("type", "Weather");
            docContent.put("title",record.getTitle());
            docContent.put("description", record.getDescription());
            docContent.put("def_group_id", researchGroupId); //create an attribute to make a relationship (Belongs to) with "ResearchGroup" entity

            String weatherID = null;
            try {
                // Create a new document
                weatherID = db.create(docContent);
                assert(weatherID != null);
                Intent resultIntent = new Intent();
                record.setWeatherId(weatherID);
                resultIntent.putExtra(Values.ADD_WEATHER_KEY, record);
                Toast.makeText(WeatherAddActivity.this, R.string.creation_ok, Toast.LENGTH_SHORT).show();
                WeatherAddActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                WeatherAddActivity.this.finish();
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(WeatherAddActivity.this, R.string.creation_failed, Toast.LENGTH_SHORT).show();
            }
// ================================================================================================================================//



//            if (ConnectionUtils.isOnline(this)) {
//                new CreateWeather(this, researchGroupId).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, record);
//            } else
//                showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * Returns valid record or null, if input values are not valid.
     *
     * @return valid record
     */
    private Weather getValidRecord() {

        EditText title = (EditText) findViewById(R.id.experiment_add_weather_title);
        EditText description = (EditText) findViewById(R.id.experiment_add_weather_description);

        StringBuilder error = new StringBuilder();

        //validations
        if (ValidationUtils.isEmpty(title.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_weather_title)).append(")").append('\n');
        if (ValidationUtils.isEmpty(description.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_weather_description)).append(")").append('\n');

        //if no error, run service
        if (error.toString().isEmpty()) {
            Weather record = new Weather();
            record.setTitle(title.getText().toString());
            record.setDescription(description.getText().toString());

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
