package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.*;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.*;
import cz.zcu.kiv.eeg.mobile.base.ui.person.PersonAddActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.scenario.ScenarioAddActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.*;

import java.util.ArrayList;
import java.util.List;

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
    private static HardwareAdapter hardwareAdapter;
    private static List<Hardware> selectedHardware = new ArrayList<Hardware>();
    private static SoftwareAdapter softwareAdapter;
    private static List<Software> selectedSoftware = new ArrayList<Software>();
    private static DiseaseAdapter diseaseAdapter;
    private static List<Disease> selectedDiseases = new ArrayList<Disease>();
    private static PharmaceuticalAdapter pharmaceuticalAdapter;
    private static List<Pharmaceutical> selectedPharmaceuticals = new ArrayList<Pharmaceutical>();
    private static ElectrodeLocationAdapter electrodeLocationAdapter;
    private static List<ElectrodeLocation> selectedElectrodeLocations = new ArrayList<ElectrodeLocation>();
    private static ElectrodeSystemAdapter electrodeSystemAdapter;
    private TimeContainer fromTime;
    private TimeContainer toTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_experiment_add);

        initView(savedInstanceState);
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
            if (electrodeSystemAdapter.isEmpty())
                updateElectrodeSystems();
        }
    }

    private void initView(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            fromTime = new TimeContainer();
            toTime = new TimeContainer();
        } else {
            fromTime = savedInstanceState.getParcelable("fromTime");
            toTime = savedInstanceState.getParcelable("toTime");
        }

        Button fromTime = (Button) findViewById(R.id.experiment_add_from_time);
        Button fromDate = (Button) findViewById(R.id.experiment_add_from_date);
        Button toTime = (Button) findViewById(R.id.experiment_add_to_time);
        Button toDate = (Button) findViewById(R.id.experiment_add_to_date);
        fromTime.setOnClickListener(this);
        fromDate.setOnClickListener(this);
        toTime.setOnClickListener(this);
        toDate.setOnClickListener(this);


        ImageButton createScenario = (ImageButton) findViewById(R.id.experiment_add_scenario_new);
        ImageButton createSubject = (ImageButton) findViewById(R.id.experiment_add_subject_new);
        ImageButton createElectrodeLocation = (ImageButton) findViewById(R.id.experiment_add_electrode_new_location_button);
        ImageButton createArtifact = (ImageButton) findViewById(R.id.experiment_add_artifact_new);
        ImageButton createDisesase = (ImageButton) findViewById(R.id.experiment_add_disease_new_button);
        ImageButton createDigitization = (ImageButton) findViewById(R.id.experiment_add_digitization_new);

        createScenario.setOnClickListener(this);
        createSubject.setOnClickListener(this);
        createElectrodeLocation.setOnClickListener(this);
        createArtifact.setOnClickListener(this);
        createDisesase.setOnClickListener(this);
        createDigitization.setOnClickListener(this);

        Spinner scenarios = (Spinner) findViewById(R.id.experiment_add_scenario);
        Spinner groups = (Spinner) findViewById(R.id.experiment_add_group);
        Spinner subjects = (Spinner) findViewById(R.id.experiment_add_subject);
        Spinner artifacts = (Spinner) findViewById(R.id.experiment_add_artifact);
        Spinner digitizations = (Spinner) findViewById(R.id.experiment_add_digitization);
        Spinner electrodeSystems = (Spinner) findViewById(R.id.experiment_add_electrode_system);


        scenarios.setAdapter(getScenarioAdapter());
        groups.setAdapter(getGroupAdapter());
        subjects.setAdapter(getPersonAdapter());
        artifacts.setAdapter(getArtifactAdapter());
        digitizations.setAdapter(getDigitizationAdapter());
        electrodeSystems.setAdapter(getElectrodeSystemAdapter());

        Button selectHw = (Button) findViewById(R.id.experiment_add_hardware_button);
        selectHw.setOnClickListener(this);
        Button selectSw = (Button) findViewById(R.id.experiment_add_software_button);
        selectSw.setOnClickListener(this);
        Button selectDiseases = (Button) findViewById(R.id.experiment_add_disease_button);
        selectDiseases.setOnClickListener(this);
        Button selectPharmaceuticals = (Button) findViewById(R.id.experiment_add_pharmaceutical_button);
        selectPharmaceuticals.setOnClickListener(this);
        Button selectElectrodeLocations = (Button) findViewById(R.id.experiment_add_electrode_location_button);
        selectElectrodeLocations.setOnClickListener(this);

        fillHardwareListRows();
        fillSoftwareListRows();
        fillDiseasesRows();
        fillPharmaceuticalsRows();
        fillElectrodeLocationsRows();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.experiment_add_from_date:
                showDateDialog((Button) findViewById(R.id.experiment_add_from_date), fromTime);
                break;
            case R.id.experiment_add_from_time:
                showTimeDialog((Button) findViewById(R.id.experiment_add_from_time), fromTime);
                break;
            case R.id.experiment_add_to_date:
                showDateDialog((Button) findViewById(R.id.experiment_add_to_date), toTime);
                break;
            case R.id.experiment_add_to_time:
                showTimeDialog((Button) findViewById(R.id.experiment_add_to_time), toTime);
                break;

            case R.id.experiment_add_scenario_new:
                intent = new Intent();
                intent.setClass(this, ScenarioAddActivity.class);
                startActivityForResult(intent, Values.ADD_SCENARIO_FLAG);
                break;

            case R.id.experiment_add_subject_new:
                intent = new Intent();
                intent.setClass(this, PersonAddActivity.class);
                startActivityForResult(intent, Values.ADD_PERSON_FLAG);
                break;

            case R.id.experiment_add_hardware_button:
                selectHardwareDialog();
                break;

            case R.id.experiment_add_software_button:
                selectSoftwareDialog();
                break;

            case R.id.experiment_add_disease_button:
                selectDiseaseDialog();
                break;

            case R.id.experiment_add_pharmaceutical_button:
                selectPharmaceuticalDialog();
                break;

            case R.id.experiment_add_electrode_location_button:
                selectElectrodeLocationsDialog();
                break;

            case R.id.experiment_add_electrode_new_location_button:
                intent = new Intent();
                intent.setClass(this, ElectrodeLocationAddActivity.class);
                startActivityForResult(intent, Values.ADD_ELECTRODE_LOCATION_FLAG);
                break;

            case R.id.experiment_add_digitization_new:
                intent = new Intent();
                intent.setClass(this, DigitizationAddActivity.class);
                startActivityForResult(intent, Values.ADD_DIGITIZATION_FLAG);
                break;
            case R.id.experiment_add_disease_new_button:
                intent = new Intent();
                intent.setClass(this, DiseaseAddActivity.class);
                startActivityForResult(intent, Values.ADD_DISEASE_FLAG);
                break;
            case R.id.experiment_add_artifact_new:
                intent = new Intent();
                intent.setClass(this, ArtifactAddActivity.class);
                startActivityForResult(intent, Values.ADD_ARTIFACT_FLAG);
                break;
        }

        super.onClick(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.exp_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!isWorking()) {
                    updateGroups();
                    updateScenarios();
                    updateSubjects();
                    updateArtifacts();
                    updateDigitizations();
                    updateElectrodeSystems();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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

            case Values.ADD_ARTIFACT_FLAG:
                if (resultCode == Activity.RESULT_OK) {
                    Artifact record = (Artifact) data.getExtras().get(Values.ADD_ARTIFACT_KEY);
                    artifactAdapter.add(record);
                }
                break;

            case Values.ADD_DIGITIZATION_FLAG:
                if (resultCode == Activity.RESULT_OK) {
                    Digitization record = (Digitization) data.getExtras().get(Values.ADD_DIGITIZATION_KEY);
                    digitizationAdapter.add(record);
                }
                break;

            case Values.ADD_DISEASE_FLAG:
                if (resultCode == Activity.RESULT_OK) {
                    Disease record = (Disease) data.getExtras().get(Values.ADD_DISEASE_KEY);
                    diseaseAdapter.add(record);
                }
                break;

            case Values.ADD_ELECTRODE_LOCATION_FLAG:
                if (resultCode == Activity.RESULT_OK) {
                    ElectrodeLocation record = (ElectrodeLocation) data.getExtras().get(Values.ADD_ELECTRODE_LOCATION_KEY);
                    electrodeLocationAdapter.add(record);
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("fromTime", fromTime);
        outState.putParcelable("toTime", toTime);
    }


    /* --------------------------------- Time choice dialogs ------------------ */

    private void showTimeDialog(final Button timeButton, final TimeContainer timeContainer) {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeContainer.setHour(hourOfDay);
                timeContainer.setMinute(minute);
                timeButton.setText(timeContainer.toTimeString());
            }
        }, timeContainer.getHour(), timeContainer.getMinute(), true).show();
    }

    private void showDateDialog(final Button dateButton, final TimeContainer timeContainer) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                timeContainer.setYear(year);
                timeContainer.setMonth(monthOfYear + 1);
                timeContainer.setDay(dayOfMonth);

                dateButton.setText(timeContainer.toDateString());

            }
        }, timeContainer.getYear(), timeContainer.getMonth() - 1, timeContainer.getDay()).show();
    }

    /* --------------------------------- CommonService invoke-methods ------------------ */

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

    private void updateHardwareList() {
        if (ConnectionUtils.isOnline(this))
            new FetchHardwareList(this, getHardwareAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    private void updateSoftwareList() {
        if (ConnectionUtils.isOnline(this))
            new FetchSoftwareList(this, getSoftwareAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    private void updateDiseases() {
        if (ConnectionUtils.isOnline(this))
            new FetchDiseases(this, getDiseaseAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    private void updatePharmaceuticals() {
        if (ConnectionUtils.isOnline(this))
            new FetchPharmaceuticals(this, getPharmaceuticalAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    private void updateElectrodeSystems() {
        if (ConnectionUtils.isOnline(this))
            new FetchElectrodeSystems(this, getElectrodeSystemAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    private void updateElectrodeLocations() {
        if (ConnectionUtils.isOnline(this))
            new FetchElectrodeLocations(this, getElectrodeLocationsAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    /* --------------------------------- Data adapters ------------------ */

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

    private HardwareAdapter getHardwareAdapter() {
        if (hardwareAdapter == null)
            hardwareAdapter = new HardwareAdapter(this, R.layout.base_hardware_row, new ArrayList<Hardware>());

        return hardwareAdapter;
    }

    private SoftwareAdapter getSoftwareAdapter() {
        if (softwareAdapter == null)
            softwareAdapter = new SoftwareAdapter(this, R.layout.base_software_row, new ArrayList<Software>());
        return softwareAdapter;
    }

    private DiseaseAdapter getDiseaseAdapter() {
        if (diseaseAdapter == null)
            diseaseAdapter = new DiseaseAdapter(this, R.layout.base_disease_row, new ArrayList<Disease>());
        return diseaseAdapter;
    }

    private PharmaceuticalAdapter getPharmaceuticalAdapter() {
        if (pharmaceuticalAdapter == null)
            pharmaceuticalAdapter = new PharmaceuticalAdapter(this, R.layout.base_pharmaceutical_row, new ArrayList<Pharmaceutical>());
        return pharmaceuticalAdapter;
    }

    private ElectrodeSystemAdapter getElectrodeSystemAdapter() {
        if (electrodeSystemAdapter == null)
            electrodeSystemAdapter = new ElectrodeSystemAdapter(this, R.layout.base_electrode_simple_row, new ArrayList<ElectrodeSystem>());
        return electrodeSystemAdapter;
    }

    private ElectrodeLocationAdapter getElectrodeLocationsAdapter() {
        if (electrodeLocationAdapter == null)
            electrodeLocationAdapter = new ElectrodeLocationAdapter(this, R.layout.base_electrode_location_row, new ArrayList<ElectrodeLocation>());
        return electrodeLocationAdapter;
    }

    /* --------------------------------- Multi-select spinners and handling their behaviour ------------------ */

    private void selectHardwareDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.base_list, null, false);
        final ListView listView = (ListView) dialogView.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setEmptyView(dialogView.findViewById(android.R.id.empty));
        listView.setAdapter(getHardwareAdapter());

        if (!isWorking() && hardwareAdapter.isEmpty())
            updateHardwareList();

        dialog.setTitle(R.string.experiment_add_hardware);
        dialog.setView(dialogView);
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();

                selectedHardware.clear();

                //find out selected items
                for (int i = 0; i < len; i++) {
                    if (checked.get(i))
                        selectedHardware.add(getHardwareAdapter().getItem(i));
                }

                fillHardwareListRows();
            }
        });

        dialog.setNeutralButton(R.string.dialog_button_clear_selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectedHardware.clear();
                fillHardwareListRows();
                Toast.makeText(ExperimentAddActivity.this, R.string.dialog_selection_cleared, Toast.LENGTH_SHORT).show();
            }
        });

        //reselect previously selected items
        for (Hardware hw : selectedHardware)
            for (int i = 0; i < hardwareAdapter.getCount(); i++) {
                if (hardwareAdapter.getItem(i).getHardwareId() == hw.getHardwareId()) {
                    listView.setItemChecked(i, true);
                }
            }
        dialog.show();
    }

    private void selectSoftwareDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.base_list, null, false);
        final ListView listView = (ListView) dialogView.findViewById(android.R.id.list);
        listView.setEmptyView(dialogView.findViewById(android.R.id.empty));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setAdapter(getSoftwareAdapter());

        if (!isWorking() && softwareAdapter.isEmpty())
            updateSoftwareList();

        dialog.setTitle(R.string.experiment_add_software);
        dialog.setView(dialogView);
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();

                selectedSoftware.clear();

                //find out selected items
                for (int i = 0; i < len; i++) {
                    if (checked.get(i))
                        selectedSoftware.add(getSoftwareAdapter().getItem(i));
                }

                fillSoftwareListRows();
            }
        });

        dialog.setNeutralButton(R.string.dialog_button_clear_selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectedSoftware.clear();
                fillSoftwareListRows();
                Toast.makeText(ExperimentAddActivity.this, R.string.dialog_selection_cleared, Toast.LENGTH_SHORT).show();
            }
        });

        //reselect previously selected items
        for (Software sw : selectedSoftware)
            for (int i = 0; i < softwareAdapter.getCount(); i++) {
                if (softwareAdapter.getItem(i).getId() == sw.getId()) {
                    listView.setItemChecked(i, true);
                }
            }
        dialog.show();
    }

    private void selectDiseaseDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.base_list, null, false);
        final ListView listView = (ListView) dialogView.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setEmptyView(dialogView.findViewById(android.R.id.empty));
        listView.setAdapter(getDiseaseAdapter());

        if (!isWorking() && diseaseAdapter.isEmpty())
            updateDiseases();

        dialog.setTitle(R.string.experiment_add_disease);
        dialog.setView(dialogView);
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();

                selectedDiseases.clear();

                //find out selected items
                for (int i = 0; i < len; i++) {
                    if (checked.get(i))
                        selectedDiseases.add(getDiseaseAdapter().getItem(i));
                }

                fillDiseasesRows();
            }
        });

        dialog.setNeutralButton(R.string.dialog_button_clear_selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectedDiseases.clear();
                fillDiseasesRows();
                Toast.makeText(ExperimentAddActivity.this, R.string.dialog_selection_cleared, Toast.LENGTH_SHORT).show();
            }
        });

        //reselect previously selected items
        for (Disease disease : selectedDiseases)
            for (int i = 0; i < diseaseAdapter.getCount(); i++) {
                if (diseaseAdapter.getItem(i).getDiseaseId() == disease.getDiseaseId()) {
                    listView.setItemChecked(i, true);
                }
            }
        dialog.show();
    }

    private void selectPharmaceuticalDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.base_list, null, false);
        final ListView listView = (ListView) dialogView.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setEmptyView(dialogView.findViewById(android.R.id.empty));
        listView.setAdapter(getPharmaceuticalAdapter());

        if (!isWorking() && pharmaceuticalAdapter.isEmpty())
            updatePharmaceuticals();

        dialog.setTitle(R.string.experiment_add_pharmaceuticals);
        dialog.setView(dialogView);
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();

                selectedPharmaceuticals.clear();

                //find out selected items
                for (int i = 0; i < len; i++) {
                    if (checked.get(i))
                        selectedPharmaceuticals.add(getPharmaceuticalAdapter().getItem(i));
                }

                fillPharmaceuticalsRows();
            }
        });

        dialog.setNeutralButton(R.string.dialog_button_clear_selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectedPharmaceuticals.clear();
                fillPharmaceuticalsRows();
                Toast.makeText(ExperimentAddActivity.this, R.string.dialog_selection_cleared, Toast.LENGTH_SHORT).show();
            }
        });

        //reselect previously selected items
        for (Pharmaceutical pharmaceutical : selectedPharmaceuticals)
            for (int i = 0; i < pharmaceuticalAdapter.getCount(); i++) {
                if (pharmaceuticalAdapter.getItem(i).getId() == pharmaceutical.getId()) {
                    listView.setItemChecked(i, true);
                }
            }
        dialog.show();
    }

    private void selectElectrodeLocationsDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.base_list, null, false);
        final ListView listView = (ListView) dialogView.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setEmptyView(dialogView.findViewById(android.R.id.empty));
        listView.setAdapter(getElectrodeLocationsAdapter());

        if (!isWorking() && electrodeLocationAdapter.isEmpty())
            updateElectrodeLocations();

        dialog.setTitle(R.string.experiment_add_electrode_location);
        dialog.setView(dialogView);
        dialog.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();

                selectedElectrodeLocations.clear();

                //find out selected items
                for (int i = 0; i < len; i++) {
                    if (checked.get(i))
                        selectedElectrodeLocations.add(getElectrodeLocationsAdapter().getItem(i));
                }

                fillElectrodeLocationsRows();
            }
        });

        dialog.setNeutralButton(R.string.dialog_button_clear_selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectedElectrodeLocations.clear();
                fillElectrodeLocationsRows();
                Toast.makeText(ExperimentAddActivity.this, R.string.dialog_selection_cleared, Toast.LENGTH_SHORT).show();
            }
        });

        //reselect previously selected items
        for (ElectrodeLocation electrodeLocation : selectedElectrodeLocations)
            for (int i = 0; i < electrodeLocationAdapter.getCount(); i++) {
                if (electrodeLocationAdapter.getItem(i).getId() == electrodeLocation.getId()) {
                    listView.setItemChecked(i, true);
                }
            }
        dialog.show();
    }

    /* --------------------------------- Methods for filling linear layout with rows from multi-select spinners ------------------ */
    private void fillHardwareListRows() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.experiment_add_hardware_list);
        //clear previous values
        layout.removeAllViews();

        ExperimentDetailLists.fillHardwareList(layout, selectedHardware);
    }

    private void fillSoftwareListRows() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.experiment_add_software_list);
        //clear previous values
        layout.removeAllViews();

        ExperimentDetailLists.fillSoftwareList(layout, selectedSoftware);
    }

    private void fillDiseasesRows() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.experiment_add_disease_list);
        //clear previous values
        layout.removeAllViews();

        ExperimentDetailLists.fillDiseaseList(layout, selectedDiseases);
    }

    private void fillPharmaceuticalsRows() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.experiment_add_pharmaceutical_list);
        //clear previous values
        layout.removeAllViews();

        ExperimentDetailLists.fillPharmaceuticals(layout, selectedPharmaceuticals);
    }

    private void fillElectrodeLocationsRows() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.experiment_add_electrode_location_list);
        //clear previous values
        layout.removeAllViews();

        ExperimentDetailLists.fillElectrodeLocations(layout, selectedElectrodeLocations);
    }

}
