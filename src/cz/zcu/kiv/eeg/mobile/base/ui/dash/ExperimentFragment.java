package cz.zcu.kiv.eeg.mobile.base.ui.dash;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.Experiment;
import cz.zcu.kiv.eeg.mobile.base.data.container.ExperimentAdapter;
import cz.zcu.kiv.eeg.mobile.base.ui.filechooser.FileChooserActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.eegbase.FetchExperiments;
import cz.zcu.kiv.eeg.mobile.base.ws.eegbase.UploadDataFile;

import java.util.ArrayList;

public class ExperimentFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = ExperimentFragment.class.getSimpleName();
    private String selectedFile = null;

    private ExperimentAdapter experimentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dash_exp, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        CommonActivity parentActivity = (CommonActivity) getActivity();

        super.onActivityCreated(savedInstanceState);
        Button fChooserButton = (Button) getActivity().findViewById(R.id.data_file_choose_button);
        Button fUploadButton = (Button) getActivity().findViewById(R.id.data_file_upload_button);
        fChooserButton.setOnClickListener(this);
        fUploadButton.setOnClickListener(this);

        experimentAdapter = new ExperimentAdapter(getActivity(), R.layout.experiment_row, new ArrayList<Experiment>());
        Spinner experimentList = (Spinner) getActivity().findViewById(R.id.experimentList);
        experimentList.setAdapter(experimentAdapter);

        final TextView descriptionTextCount = (TextView) getActivity().findViewById(R.id.datafile_description_count);

        final TextWatcher datafileDescriptionWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    descriptionTextCount.setVisibility(View.VISIBLE);
                    descriptionTextCount.setText(getString(R.string.data_file_left) + (getResources().getInteger(R.integer.limit_datafile_description_chars) - s.length()));
                } else {
                    descriptionTextCount.setVisibility(View.INVISIBLE);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        EditText descriptionText = (EditText) getActivity().findViewById(R.id.datafile_description);
        descriptionText.addTextChangedListener(datafileDescriptionWatcher);

        if (ConnectionUtils.isOnline(getActivity())) {
            (CommonActivity.service) = (CommonService) new FetchExperiments(parentActivity, experimentAdapter).execute();
        } else
            parentActivity.showAlert(getString(R.string.error_offline));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_file_choose_button:
                Log.d(TAG, "Choosing file");
                Intent intent = new Intent(getActivity(), FileChooserActivity.class);
                startActivityForResult(intent, Values.SELECT_FILE_FLAG);
                break;
            case R.id.data_file_upload_button:
                uploadDataFile();
                break;
        }
    }

    private void uploadDataFile() {
        EditText fileDescription = (EditText) getActivity().findViewById(R.id.datafile_description);
        Spinner experiments = (Spinner) getActivity().findViewById(R.id.experimentList);
        Experiment exp = (Experiment) experiments.getSelectedItem();

        if (selectedFile == null) {
            Toast.makeText(this.getActivity(), "No file for upload selected!", Toast.LENGTH_SHORT).show();
        } else if (exp == null) {
            Toast.makeText(this.getActivity(), "No experiment selected!", Toast.LENGTH_SHORT).show();
        } else {
            new UploadDataFile((CommonActivity) getActivity()).execute(""+exp.getExperimentId(), fileDescription.getEditableText().toString(), selectedFile);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Values.SELECT_FILE_FLAG): {
                if (resultCode == Activity.RESULT_OK) {
                    selectedFile = data.getExtras().getString(Values.FILE_PATH);
                    Toast.makeText(this.getActivity(), selectedFile, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
