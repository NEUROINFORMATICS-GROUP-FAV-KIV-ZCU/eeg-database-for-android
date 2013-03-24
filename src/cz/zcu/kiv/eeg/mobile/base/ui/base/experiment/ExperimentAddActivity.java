package cz.zcu.kiv.eeg.mobile.base.ui.base.experiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.PersonAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Person;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.ui.base.person.PersonAddActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.base.scenario.ScenarioAddActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchPeople;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchResearchGroups;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchScenarios;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_experiment_add);

        initView();
        updateData();
    }

    private void updateData() {
        updateGroups();
        updateScenarios();
        updateSubjects();
    }

    private void initView() {
        ImageButton createScenario = (ImageButton) findViewById(R.id.experiment_add_scenario_new);
        ImageButton createSubject = (ImageButton) findViewById(R.id.experiment_add_subject_new);

        createScenario.setOnClickListener(this);
        createSubject.setOnClickListener(this);

        Spinner scenarios = (Spinner) findViewById(R.id.experiment_add_scenario);
        Spinner groups = (Spinner) findViewById(R.id.experiment_add_group);
        Spinner subjects = (Spinner) findViewById(R.id.experiment_add_subject);


        scenarios.setAdapter(getScenarioAdapter());
        groups.setAdapter(getGroupAdapter());
        subjects.setAdapter(getPersonAdapter());
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
            (ExperimentAddActivity.service) = (CommonService) new FetchScenarios(this, getScenarioAdapter(), Values.SERVICE_QUALIFIER_MINE).execute();
        } else
            showAlert(getString(R.string.error_offline));
    }

    private void updateGroups() {
        if (ConnectionUtils.isOnline(this)) {
            (ExperimentAddActivity.service) = (CommonService) new FetchResearchGroups(this, getGroupAdapter(), Values.SERVICE_QUALIFIER_MINE).execute();
        } else
            showAlert(getString(R.string.error_offline));
    }

    private void updateSubjects() {
        if (ConnectionUtils.isOnline(this)) {
            (ExperimentAddActivity.service) = (CommonService) new FetchPeople(this, getPersonAdapter()).execute();
        } else
            showAlert(getString(R.string.error_offline));
    }

    private ScenarioAdapter getScenarioAdapter() {
        if (scenarioAdapter == null) {
            scenarioAdapter = new ScenarioAdapter(this, R.layout.base_scenario_row, new ArrayList<Scenario>());
        }
        return scenarioAdapter;
    }

    public ResearchGroupAdapter getGroupAdapter() {
        if (groupAdapter == null) {
            groupAdapter = new ResearchGroupAdapter(this, R.layout.base_row_simple, new ArrayList<ResearchGroup>());
        }

        return groupAdapter;
    }

    public PersonAdapter getPersonAdapter() {
        if (personAdapter == null)
            personAdapter = new PersonAdapter(this, R.layout.base_person_row, new ArrayList<Person>());

        return personAdapter;
    }
}
