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
    private boolean empty = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            empty = false;
            return inflater.inflate(R.layout.base_experiment_details, container, false);
        } else {
            empty = true;
            return inflater.inflate(R.layout.details_empty, container, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        if (!empty) {
            initData();
        }
    }

    private void initData() {
        TextView experimentIdView = (TextView) getActivity().findViewById(R.id.experimentIdValue);
        TextView scenarioNameView = (TextView) getActivity().findViewById(R.id.scenarioNameValue);
        TextView fromTime = (TextView) getActivity().findViewById(R.id.fromValue);
        TextView toTime = (TextView) getActivity().findViewById(R.id.toValue);

        SimpleDateFormat sf = new SimpleDateFormat("HH:mm dd.MM.yy");
        Experiment experiment = (Experiment) getArguments().getSerializable("data");
        if (experiment != null) {
            experimentIdView.setText(""+experiment.getExperimentId());
            fromTime.setText(sf.format(experiment.getStartTime()));
            toTime.setText(sf.format(experiment.getEndTime()));
            scenarioNameView.setText(experiment.getScenarioName());

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
