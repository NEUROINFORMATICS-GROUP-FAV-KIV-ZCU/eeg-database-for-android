package cz.zcu.kiv.eeg.mobile.base.ui.datafile;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ExperimentAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;
import cz.zcu.kiv.eeg.mobile.base.ui.filechooser.FileChooserActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchExperiments;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.UploadDataFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Fragment for uploading DataFile object into experiment.
 *
 * @author Petr Miko
 */
public class DataFileUploadFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = DataFileUploadFragment.class.getSimpleName();
    private String selectedFile = null;
    private ExperimentAdapter experimentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_data_file_upload, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        CommonActivity parentActivity = (CommonActivity) getActivity();
        super.onActivityCreated(savedInstanceState);
        initData(parentActivity);
    }

    /**
     * Initializes layout view elements.
     * Sets buttons' onClickListeners, adapter for experiments spinner and sets limit for description text length.
     *
     * @param inflatedView view to be displayed
     */
    private void initView(View inflatedView) {
        //getting layout elements
        Button fChooserButton = (Button) inflatedView.findViewById(R.id.data_file_choose_button);
        Button fUploadButton = (Button) inflatedView.findViewById(R.id.data_file_upload_button);
        Spinner experimentList = (Spinner) inflatedView.findViewById(R.id.experimentList);
        final TextView descriptionTextCount = (TextView) inflatedView.findViewById(R.id.datafile_description_count);
        EditText descriptionText = (EditText) inflatedView.findViewById(R.id.datafile_description);

        //setting onClickListener
        fChooserButton.setOnClickListener(this);
        fUploadButton.setOnClickListener(this);

        //setting exp adapter
        experimentAdapter = new ExperimentAdapter(getActivity(), R.layout.base_experiment_row, new ArrayList<Experiment>());
        experimentList.setAdapter(experimentAdapter);

        //limiting possible input length
        descriptionText.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_description_chars), descriptionTextCount));
    }

    /**
     * Initializes view data if online.
     * Uses FetchExperimentsService.
     *
     * @param parentActivity parent CommonActivity
     */
    private void initData(CommonActivity parentActivity) {
        if (ConnectionUtils.isOnline(parentActivity)) {
            new FetchExperiments(parentActivity, experimentAdapter, Values.SERVICE_QUALIFIER_MINE).execute();
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

    /**
     * Checks whether there is file and experiment selected.
     * If so, data file is created and uploaded.
     * Uses UploadDataFile service.
     */
    private void uploadDataFile() {

        CommonActivity activity = (CommonActivity) getActivity();

        if (!ConnectionUtils.isOnline(activity)) {
            activity.showAlert(getString(R.string.error_offline));
            return;
        }

        EditText fileDescription = (EditText) getActivity().findViewById(R.id.datafile_description);
        Spinner experiments = (Spinner) getActivity().findViewById(R.id.experimentList);
        Experiment exp = (Experiment) experiments.getSelectedItem();

        if (selectedFile == null) {
            activity.showAlert(getString(R.string.error_no_file_selected));
        } else if (exp == null) {
            activity.showAlert(getString(R.string.error_no_experiment_selected));
        } else {
            new UploadDataFile((CommonActivity) getActivity()).execute(Integer.toString(exp.getExperimentId()), fileDescription.getEditableText().toString(), selectedFile);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // obtaining path and name of file selected in FileChooserActivity
            case (Values.SELECT_FILE_FLAG): {
                if (resultCode == Activity.RESULT_OK) {
                    selectedFile = data.getExtras().getString(Values.FILE_PATH);
                    TextView selectedFileView = (TextView) getActivity().findViewById(R.id.fchooserSelectedFile);
                    selectedFileView.setText((new File(selectedFile)).getName());
                    Toast.makeText(this.getActivity(), selectedFile, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    /**
     * Adds to options menu search possibility.
     *
     * @param menu     menu to extend
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.data_add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                initData((CommonActivity) getActivity());
                Log.d(TAG, "Refresh data button pressed");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
