package cz.zcu.kiv.eeg.mobile.base.ui.base.experiment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.Experiment;

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

        SimpleDateFormat sf = new SimpleDateFormat("HH:mm dd.MM.yy");
        Experiment experiment = (Experiment) getArguments().getSerializable("data");
        if (experiment != null) {
            experimentIdView.setText(Integer.toString(experiment.getExperimentId()));
            fromTime.setText(sf.format(experiment.getStartTime()));
            toTime.setText(sf.format(experiment.getEndTime()));
            scenarioNameView.setText(experiment.getScenarioName());
            envNote.setText(experiment.getEnvironmentNote());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
