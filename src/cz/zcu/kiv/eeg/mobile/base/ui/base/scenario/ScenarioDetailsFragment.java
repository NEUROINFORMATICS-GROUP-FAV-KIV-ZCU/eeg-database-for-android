package cz.zcu.kiv.eeg.mobile.base.ui.base.scenario;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.Scenario;
import cz.zcu.kiv.eeg.mobile.base.utils.FileUtils;

@SuppressLint("SimpleDateFormat")
public class ScenarioDetailsFragment extends Fragment {

    public final static String TAG = ScenarioDetailsFragment.class.getSimpleName();
    private boolean empty = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            empty = false;
            return inflater.inflate(R.layout.base_scenario_details, container, false);
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

        TextView scenarioId = (TextView) getActivity().findViewById(R.id.scenarioId);
        TextView scenarioName = (TextView) getActivity().findViewById(R.id.scenarioName);
        TextView scenarioType = (TextView) getActivity().findViewById(R.id.scenarioMime);
        TextView fileName = (TextView) getActivity().findViewById(R.id.scenarioFileName);
        TextView fileSize = (TextView) getActivity().findViewById(R.id.scenarioFileLength);
        TextView description = (TextView) getActivity().findViewById(R.id.scenarioDescription);

        Scenario scenario = (Scenario) getArguments().getSerializable("data");
        if (scenario != null) {
            scenarioId.setText(scenario.getScenarioId());
            scenarioName.setText(scenario.getScenarioName());
            scenarioType.setText(scenario.getMimeType());
            fileName.setText(scenario.getFileName());
            fileSize.setText(FileUtils.getFileSize(Long.parseLong(scenario.getFileLength())));
            description.setText(scenario.getDescription());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
