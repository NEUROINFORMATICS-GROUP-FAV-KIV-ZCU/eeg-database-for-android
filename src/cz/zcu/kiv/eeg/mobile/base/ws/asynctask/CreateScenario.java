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
package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for creating new scenario on eeg base.
 *
 * @author Petr Miko
 */
public class CreateScenario extends CommonService<Scenario, Void, Scenario> {

    private final static String TAG = CreateScenario.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateScenario(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where scenario information is pushed to server in order to new scenario.
     * All heavy lifting is made here.
     *
     * @param scenarios only one scenario is accepted - scenario to be uploaded
     * @return scenario stored
     */
    @Override
    protected Scenario doInBackground(Scenario... scenarios) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_SCENARIOS;

        setState(RUNNING, R.string.working_ws_create_scenario);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));


        SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
        //so files wont buffer in memory
        factory.setBufferRequestBody(false);
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());


        Scenario scenario = scenarios[0];

        try {
            Log.d(TAG, url);
            FileSystemResource toBeUploadedFile = new FileSystemResource(scenario.getFilePath());

            //due to multipart file, MultiValueMap is the simplest approach for performing the post request
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
            form.add("scenarioName", scenario.getScenarioName());
            form.add("researchGroupId", scenario.getResearchGroupId());
            form.add("description", scenario.getDescription());
            form.add("mimeType", scenario.getMimeType());
            form.add("private", Boolean.toString(scenario.isPrivate()));
            form.add("file", toBeUploadedFile);

            HttpEntity<Object> entity = new HttpEntity<Object>(form, requestHeaders);
            // Make the network request
            ResponseEntity<Scenario> response = restTemplate.postForEntity(url, entity, Scenario.class);
            return response.getBody();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    /**
     * If scenario was created successfully, URI to scenario file will be displayed shortly.
     *
     * @param scenario created scenario if any
     */
    @Override
    protected void onPostExecute(Scenario scenario) {
        if (scenario != null) {

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_SCENARIO_KEY, scenario);
            activity.setResult(Activity.RESULT_OK, resultIntent);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.finish();

        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
