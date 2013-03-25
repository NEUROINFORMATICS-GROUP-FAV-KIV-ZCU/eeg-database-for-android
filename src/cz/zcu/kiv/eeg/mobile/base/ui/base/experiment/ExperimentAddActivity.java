package cz.zcu.kiv.eeg.mobile.base.ui.base.experiment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.*;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.*;
import cz.zcu.kiv.eeg.mobile.base.ui.base.person.PersonAddActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.base.scenario.ScenarioAddActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.*;

import java.util.ArrayList;

/**
 * Activity for creating new experiment on eeg base.
 *
 * @author Petr Miko
 */
public class ExperimentAddActivity extends SaveDiscardActivity implements View.OnClickListener {

    private static ResearchGroupAdapter groupAdapter;
    private static ScenarioAdapter scenarioAdapter;
    private static PersonAdapter personAdapter;
    private static ArtifactAdapter artifactAdapter;
    private static DigitizationAdapter digitizationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_experiment_add);

        initView();
        updateData();
    }

    private void updateData() {
        //services are not loading data currently
        if (!isWorking()) {
            if (groupAdapter.isEmpty())
                updateGroups();
            if (scenarioAdapter.isEmpty())
                updateScenarios();
            if (personAdapter.isEmpty())
                updateSubjects();
            if (artifactAdapter.isEmpty())
                updateArtifacts();
            if (digitizationAdapter.isEmpty())
                updateDigitizations();
        }
    }

    private void initView() {
        ImageButton createScenario = (ImageButton) findViewById(R.id.experiment_add_scenario_new);
        ImageButton createSubject = (ImageButton) findViewById(R.id.experiment_add_subject_new);

        createScenario.setOnClickListener(this);
        createSubject.setOnClickListener(this);

        Spinner scenarios = (Spinner) findViewById(R.id.experiment_add_scenario);
        Spinner groups = (Spinner) findViewById(R.id.experiment_add_group);
        Spinner subjects = (Spinner) findViewById(R.id.experiment_add_subject);
        Spinner artifacts = (Spinner) findViewById(R.id.experiment_add_artifact);
        Spinner digitizations = (Spinner) findViewById(R.id.experiment_add_digitization);


        scenarios.setAdapter(getScenarioAdapter());
        groups.setAdapter(getGroupAdapter());
        subjects.setAdapter(getPersonAdapter());
        artifacts.setAdapter(getArtifactAdapter());
        digitizations.setAdapter(getDigitizationAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.experiment_add_scenario_new:
                Intent scenarioAddIntent = new Intent();
                scenarioAddIntent.setClass(this, ScenarioAddActivity.class);
                startActivityForResult(scenarioAddIntent, Values.ADD_SCENARIO_FLAG);
                break;

            case R.id.experiment_add_subject_new:
                Intent personAddIntent = new Intent();
                personAddIntent.setClass(this, PersonAddActivity.class);
                startActivityForResult(personAddIntent, Values.ADD_PERSON_FLAG);
                break;
        }

        super.onClick(v);
    }

    @Override
    protected void save() {
        Toast.makeText(this, "not implemented yet", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void discard() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Values.ADD_SCENARIO_FLAG:
                if (resultCode == Activity.RESULT_OK) {
                    Scenario record = (Scenario) data.getExtras().get(Values.ADD_SCENARIO_KEY);
                    scenarioAdapter.add(record);
                }
                break;
            case Values.ADD_PERSON_FLAG:
                if (resultCode == Activity.RESULT_OK) {
                    Person record = (Person) data.getExtras().get(Values.ADD_PERSON_KEY);
                    personAdapter.add(record);
                }
                break;
        }
    }

    private void updateScenarios() {
        if (ConnectionUtils.isOnline(this)) {
            new FetchScenarios(this, getScenarioAdapter(), Values.SERVICE_QUALIFIER_MINE).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else
            showAlert(getString(R.string.error_offline));
    }

    private void updateGroups() {
        if (ConnectionUtils.isOnline(this)) {
            new FetchResearchGroups(this, getGroupAdapter(), Values.SERVICE_QUALIFIER_MINE).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else
            showAlert(getString(R.string.error_offline));
    }

    private void updateSubjects() {
        if (ConnectionUtils.isOnline(this)) {
            new FetchPeople(this, getPersonAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else
            showAlert(getString(R.string.error_offline));
    }

    private void updateArtifacts() {
        if (ConnectionUtils.isOnline(this)) {
            new FetchArtifacts(this, getArtifactAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else
            showAlert(getString(R.string.error_offline));
    }

    private void updateDigitizations() {
        if (ConnectionUtils.isOnline(this)) {
            new FetchDigitizations(this, getDigitizationAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else
            showAlert(getString(R.string.error_offline));
    }

    private ScenarioAdapter getScenarioAdapter() {
        if (scenarioAdapter == null) {
            scenarioAdapter = new ScenarioAdapter(this, R.layout.base_scenario_row, new ArrayList<Scenario>());
        }
        return scenarioAdapter;
    }

    private ResearchGroupAdapter getGroupAdapter() {
        if (groupAdapter == null) {
            groupAdapter = new ResearchGroupAdapter(this, R.layout.base_row_simple, new ArrayList<ResearchGroup>());
        }

        return groupAdapter;
    }

    private PersonAdapter getPersonAdapter() {
        if (personAdapter == null)
            personAdapter = new PersonAdapter(this, R.layout.base_person_row, new ArrayList<Person>());

        return personAdapter;
    }

    private ArtifactAdapter getArtifactAdapter() {
        if (artifactAdapter == null)
            artifactAdapter = new ArtifactAdapter(this, R.layout.base_artifact_row, new ArrayList<Artifact>());

        return artifactAdapter;
    }

    private DigitizationAdapter getDigitizationAdapter() {
        if (digitizationAdapter == null)
            digitizationAdapter = new DigitizationAdapter(this, R.layout.base_digitization_row, new ArrayList<Digitization>());

        return digitizationAdapter;
    }
}
