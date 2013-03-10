package cz.zcu.kiv.eeg.mobile.base.ui.base.experiment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.*;

import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class ExperimentDetailsFragment extends Fragment {

    public final static String TAG = ExperimentDetailsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean hasData = getArguments().getSerializable("data") != null && getArguments().getInt("index", -1) >= 0;

        if (hasData) {
            View view = inflater.inflate(R.layout.base_experiment_details, container, false);
            initData(view);
            return view;
        } else {
            return inflater.inflate(R.layout.details_empty, container, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
    }

    private void initData(View view) {
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

        SimpleDateFormat sf = new SimpleDateFormat("HH:mm dd.MM.yy");
        Experiment experiment = (Experiment) getArguments().getSerializable("data");
        if (experiment != null) {
            experimentIdView.setText(Integer.toString(experiment.getExperimentId()));
            fromTime.setText(sf.format(experiment.getStartTime()));
            toTime.setText(sf.format(experiment.getEndTime()));
            scenarioNameView.setText(experiment.getScenarioName());
            envNote.setText(experiment.getEnvironmentNote());

            Person subject = experiment.getSubject();
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
        }
    }

    private void fillHardwareList(LinearLayout hardwareList, Experiment experiment) {
        if (experiment.getHardwares() != null) {

            for (Hardware record : experiment.getHardwares()) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View row = inflater.inflate(R.layout.base_hardware_row, hardwareList, false);

                TextView hwId = (TextView) row.findViewById(R.id.row_hardware_id);
                TextView hwTitle = (TextView) row.findViewById(R.id.row_hardware_title);
                TextView hwType = (TextView) row.findViewById(R.id.row_hardware_type);
                TextView hwDescription = (TextView) row.findViewById(R.id.row_hardware_description);

                if (hwId != null) {
                    hwId.setText(Integer.toString(record.getId()));
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
        } else{
            TextView row = new TextView(getActivity());
            row.setText(R.string.dummy_none);
            hardwareList.addView(row);
        }
    }

    private void fillDiseases(LinearLayout diseasesList, Experiment experiment) {
        if (experiment.getDiseases() != null) {

            for (Disease record : experiment.getDiseases()) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
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

        } else{
            TextView row = new TextView(getActivity());
            row.setText(R.string.dummy_none);
            diseasesList.addView(row);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
