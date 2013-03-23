package cz.zcu.kiv.eeg.mobile.base.ui.base.experiment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.*;

/**
 * Fragment used for displaying details about chosen experiment.
 *
 * @author Petr Miko
 */
@SuppressLint("SimpleDateFormat")
public class ExperimentDetailsFragment extends Fragment {

    public final static String TAG = ExperimentDetailsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //check if data are available and selection index is positive
        boolean hasData = getArguments().getParcelable("data") != null && getArguments().getInt("index", -1) >= 0;

        if (hasData) {
            View view = inflater.inflate(R.layout.base_experiment_details, container, false);
            initData(view);
            return view;
        } else {
            return inflater.inflate(R.layout.details_empty, container, false);
        }
    }

    /**
     * Method for filling element views with proper data.
     *
     * @param view object of layout, which contains element child views
     */
    private void initData(View view) {
        //get all view elements
        TextView experimentIdView = (TextView) view.findViewById(R.id.experimentIdValue);
        TextView scenarioNameView = (TextView) view.findViewById(R.id.scenarioNameValue);
        TextView fromTime = (TextView) view.findViewById(R.id.fromValue);
        TextView toTime = (TextView) view.findViewById(R.id.toValue);
        TextView envNote = (TextView) view.findViewById(R.id.experiment_env_note);
        TextView subjectName = (TextView) view.findViewById(R.id.experiment_subject);
        TextView subjectAge = (TextView) view.findViewById(R.id.experiment_subject_age);
        TextView subjectLeftHand = (TextView) view.findViewById(R.id.experiment_subject_lefthand);
        TextView subjectGender = (TextView) view.findViewById(R.id.experiment_subject_gender);
        TextView weatherTitle = (TextView) view.findViewById(R.id.experiment_weather_title);
        TextView weatherDescription = (TextView) view.findViewById(R.id.experiment_weather_description);
        TextView artifactCompensation = (TextView) view.findViewById(R.id.experiment_artifact_compensation);
        TextView artifactRejectCond = (TextView) view.findViewById(R.id.experiment_artifact_reject);
        TextView digitizationFilter = (TextView) view.findViewById(R.id.experiment_digitization_filter);
        TextView digitizationGain = (TextView) view.findViewById(R.id.experiment_digitization_gain);
        TextView digitizationRate = (TextView) view.findViewById(R.id.experiment_digitization_sampling_rate);
        TextView temperature = (TextView) view.findViewById(R.id.experiment_temperature);
        LinearLayout diseasesList = (LinearLayout) view.findViewById(R.id.experiment_diseases);
        LinearLayout hardwareList = (LinearLayout) view.findViewById(R.id.experiment_hardware_list);
        LinearLayout softwareList = (LinearLayout) view.findViewById(R.id.experiment_softwares);
        LinearLayout pharmaceuticals = (LinearLayout) view.findViewById(R.id.experiment_pharmaceuticals);
        TextView electrodeImpendace = (TextView) view.findViewById(R.id.experiment_electrode_impedance);
        TextView electrodeSystemDescription = (TextView) view.findViewById(R.id.experiment_electrode_system_description);
        TextView electrodeSystemName = (TextView) view.findViewById(R.id.experiment_electrode_system_title);
        LinearLayout electrodeLocations = (LinearLayout) view.findViewById(R.id.experiment_electrodes);

        Experiment experiment = (Experiment) getArguments().getParcelable("data");

        //setting data
        if (experiment != null) {
            experimentIdView.setText(Integer.toString(experiment.getExperimentId()));
            fromTime.setText(experiment.getStartTime());
            toTime.setText(experiment.getEndTime());
            scenarioNameView.setText(experiment.getScenario().getScenarioName());
            envNote.setText(experiment.getEnvironmentNote());

            Subject subject = experiment.getSubject();
            subjectName.setText(subject.getName() + " " + subject.getSurname());
            subjectGender.setText(subject.getGender());
            subjectAge.setText(Integer.toString(subject.getAge()));
            subjectLeftHand.setText(getString(subject.isLeftHanded() ? R.string.yes : R.string.no));

            Weather weather = experiment.getWeather();
            weatherTitle.setText(weather.getTitle());
            weatherDescription.setText(weather.getDescription());

            Artifact artifact = experiment.getArtifact();
            artifactCompensation.setText(artifact.getCompensation());
            artifactRejectCond.setText(artifact.getRejectCondition());

            Digitization digitization = experiment.getDigitization();
            digitizationFilter.setText(digitization.getFilter());
            digitizationGain.setText(Float.toString(digitization.getGain()));
            digitizationRate.setText(Float.toString(digitization.getSamplingRate()));

            if (experiment.getTemperature() != null) {
                temperature.setText(experiment.getTemperature().toString());
            }

            fillDiseases(diseasesList, experiment);
            fillHardwareList(hardwareList, experiment);
            fillSoftwareList(softwareList, experiment);
            fillPharmaceuticals(pharmaceuticals, experiment);
            fillElectroders(electrodeImpendace, electrodeSystemName, electrodeSystemDescription, electrodeLocations, experiment);
        }
    }

    /**
     * Method for displaying electrode information.
     *
     * @param electrodeImpendace         set impedance
     * @param electrodeSystemName        name of electrode system
     * @param electrodeSystemDescription electrode system description
     * @param electrodeLocations         frame, where will be all electrode locations data inflated in
     * @param experiment                 original source object
     */
    private void fillElectroders(TextView electrodeImpendace, TextView electrodeSystemName, TextView electrodeSystemDescription, LinearLayout electrodeLocations, Experiment experiment) {

        ElectrodeConf electrodeConf = experiment.getElectrodeConf();
        electrodeImpendace.setText(Integer.toString(electrodeConf.getImpedance()));
        ElectrodeSystem electrodeSystem = electrodeConf.getElectrodeSystem();
        electrodeSystemDescription.setText(electrodeSystem.getDescription());
        electrodeSystemName.setText(electrodeSystem.getTitle());

        ElectrodeLocationList electrodeLocationList = electrodeConf.getElectrodeLocations();

        if (electrodeLocationList != null && electrodeLocationList.isAvailable()) {

            //create and inflate electrode locations row by row
            final LayoutInflater inflater = getActivity().getLayoutInflater();

            for (final ElectrodeLocation eLocation : electrodeLocationList.getElectrodeLocations()) {
                View row = inflater.inflate(R.layout.base_electrode_location_row, electrodeLocations, false);

                TextView electrodeId = (TextView) row.findViewById(R.id.row_electrode_location_id);
                TextView electrodeAbbr = (TextView) row.findViewById(R.id.row_electrode_location_abbr);
                TextView electrodeTitle = (TextView) row.findViewById(R.id.row_electrode_location_title);
                TextView electrodeDescription = (TextView) row.findViewById(R.id.row_electrode_location_description);

                if (electrodeId != null) {
                    electrodeId.setText(Integer.toString(eLocation.getId()));
                }

                if (electrodeAbbr != null) {
                    electrodeAbbr.setText(eLocation.getAbbr());
                }

                if (electrodeTitle != null) {
                    electrodeTitle.setText(eLocation.getTitle());
                }

                if (electrodeDescription != null) {
                    electrodeDescription.setText(eLocation.getDescription());
                }

                electrodeLocations.addView(row);

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.experiment_electrode_detail);
                        View view = inflater.inflate(R.layout.base_electrode_dialog_details, null);

                        TextView id = (TextView) view.findViewById(R.id.experiment_electrode_id);
                        TextView abbr = (TextView) view.findViewById(R.id.experiment_electrode_abbr);
                        TextView title = (TextView) view.findViewById(R.id.experiment_electrode_title);
                        TextView description = (TextView) view.findViewById(R.id.experiment_electrode_description);

                        TextView typeTitle = (TextView) view.findViewById(R.id.experiment_electrode_type_title);
                        TextView typeDescription = (TextView) view.findViewById(R.id.experiment_electrode_type_description);
                        TextView fixTitle = (TextView) view.findViewById(R.id.experiment_electrode_fix_title);
                        TextView fixDescription = (TextView) view.findViewById(R.id.experiment_electrode_fix_description);

                        id.setText(Integer.toString(eLocation.getId()));
                        title.setText(eLocation.getTitle());
                        abbr.setText(eLocation.getAbbr());
                        description.setText(eLocation.getDescription());
                        typeTitle.setText(eLocation.getElectrodeType().getTitle());
                        typeDescription.setText(eLocation.getElectrodeType().getDescription());
                        fixTitle.setText(eLocation.getElectrodeFix().getTitle());
                        fixDescription.setText(eLocation.getElectrodeFix().getDescription());

                        builder.setView(view);
                        builder.show();
                    }
                });
            }
        } else {
            //inflate information, that no record is available
            TextView row = new TextView(getActivity());
            row.setText(R.string.dummy_none);
            electrodeLocations.addView(row);
        }
    }

    /**
     * Fills view for displaying pharmaceuticals.
     *
     * @param pharmaceuticals layout element into which should be element child views inflated
     * @param experiment      experiment object for accessing data
     */
    private void fillPharmaceuticals(LinearLayout pharmaceuticals, Experiment experiment) {
        if (experiment.getPharmaceuticals() != null && experiment.getPharmaceuticals().isAvailable()) {
            //create and inflate row by row
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for (Pharmaceutical record : experiment.getPharmaceuticals().getPharmaceuticals()) {
                View row = inflater.inflate(R.layout.base_pharmaceutical_row, pharmaceuticals, false);

                TextView pharmId = (TextView) row.findViewById(R.id.row_software_id);
                TextView pharmTitle = (TextView) row.findViewById(R.id.row_software_title);
                TextView pharmDescription = (TextView) row.findViewById(R.id.row_software_description);

                if (pharmId != null) {
                    pharmId.setText(Integer.toString(record.getId()));
                }
                if (pharmTitle != null) {
                    pharmTitle.setText(record.getTitle());
                }
                if (pharmDescription != null) {
                    pharmDescription.setText(record.getDescription());
                }
                pharmaceuticals.addView(row);
            }
        } else {
            //inflate information, that no record is available
            TextView row = new TextView(getActivity());
            row.setText(R.string.dummy_none);
            pharmaceuticals.addView(row);
        }
    }

    /**
     * Fills view for displaying software list.
     *
     * @param softwareList layout element into which should be element child views inflated
     * @param experiment   experiment object for accessing data
     */
    private void fillSoftwareList(LinearLayout softwareList, Experiment experiment) {
        if (experiment.getSoftwareList() != null && experiment.getSoftwareList().isAvailable()) {

            //create and inflate row by row
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for (Software record : experiment.getSoftwareList().getSoftwareList()) {
                View row = inflater.inflate(R.layout.base_software_row, softwareList, false);

                TextView swId = (TextView) row.findViewById(R.id.row_software_id);
                TextView swTitle = (TextView) row.findViewById(R.id.row_software_title);
                TextView swDescription = (TextView) row.findViewById(R.id.row_software_description);

                if (swId != null) {
                    swId.setText(Integer.toString(record.getId()));
                }
                if (swTitle != null) {
                    swTitle.setText(record.getTitle());
                }
                if (swDescription != null) {
                    swDescription.setText(record.getDescription());
                }
                softwareList.addView(row);
            }
        } else {
            //inflate information, that no record is available
            TextView row = new TextView(getActivity());
            row.setText(R.string.dummy_none);
            softwareList.addView(row);
        }
    }

    /**
     * Fills view for displaying hardware list.
     *
     * @param hardwareList layout element into which should be element child views inflated
     * @param experiment   experiment object for accessing data
     */
    private void fillHardwareList(LinearLayout hardwareList, Experiment experiment) {
        if (experiment.getHardwareList() != null && experiment.getHardwareList().isAvailable()) {

            //create and inflate row by row
            LayoutInflater inflater = getActivity().getLayoutInflater();

            for (Hardware record : experiment.getHardwareList().getHardwareList()) {
                View row = inflater.inflate(R.layout.base_hardware_row, hardwareList, false);

                TextView hwId = (TextView) row.findViewById(R.id.row_hardware_id);
                TextView hwTitle = (TextView) row.findViewById(R.id.row_hardware_title);
                TextView hwType = (TextView) row.findViewById(R.id.row_hardware_type);
                TextView hwDescription = (TextView) row.findViewById(R.id.row_hardware_description);

                if (hwId != null) {
                    hwId.setText(Integer.toString(record.getHardwareId()));
                }
                if (hwTitle != null) {
                    hwTitle.setText(record.getTitle());
                }
                if (hwType != null) {
                    hwType.setText(record.getType());
                }
                if (hwDescription != null) {
                    hwDescription.setText(record.getDescription());
                }
                hardwareList.addView(row);
            }
        } else {
            //inflate information, that no record is available
            TextView row = new TextView(getActivity());
            row.setText(R.string.dummy_none);
            hardwareList.addView(row);
        }
    }

    /**
     * Method for inflating disease information into its layout element.
     *
     * @param diseasesList layout element into which should be element child views inflated
     * @param experiment   experiment object for accessing data
     */
    private void fillDiseases(LinearLayout diseasesList, Experiment experiment) {
        if (experiment.getDiseases() != null && experiment.getDiseases().isAvailable()) {

            //create and inflate row by row
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for (Disease record : experiment.getDiseases().getDiseases()) {
                View row = inflater.inflate(R.layout.base_disease_row, diseasesList, false);

                TextView diseaseName = (TextView) row.findViewById(R.id.row_disease_name);
                TextView diseaseDescription = (TextView) row.findViewById(R.id.row_disease_description);

                if (diseaseName != null) {
                    diseaseName.setText(record.getName());
                }
                if (diseaseDescription != null) {
                    diseaseDescription.setText(record.getDescription());
                }
                diseasesList.addView(row);
            }

        } else {
            //inflate information, that no record is available
            TextView row = new TextView(getActivity());
            row.setText(R.string.dummy_none);
            diseasesList.addView(row);
        }

    }
}
