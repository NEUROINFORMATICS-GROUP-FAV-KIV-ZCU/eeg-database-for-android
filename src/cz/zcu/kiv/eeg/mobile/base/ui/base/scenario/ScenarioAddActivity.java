package cz.zcu.kiv.eeg.mobile.base.ui.base.scenario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.ui.filechooser.FileChooserActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.FileUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateScenario;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchResearchGroups;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;

/**
 * Activity for creating new Scenario record on eeg base.
 *
 * @author Petr Miko
 */
public class ScenarioAddActivity extends SaveDiscardActivity implements View.OnClickListener {

    private static final String TAG = ScenarioAddActivity.class.getSimpleName();
    private String selectedFile;
    private ResearchGroupAdapter researchGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_scenario_add);

        ImageButton chooseFileButton = (ImageButton) findViewById(R.id.scenario_file_button);
        chooseFileButton.setOnClickListener(this);
        initView();
        updateData();
    }

    /**
     * Method for initializing layout elements.
     * Sets limit for description text length and an adapter for research group list.
     */
    private void initView() {
        //obtaining view elements
        final TextView descriptionCountText = (TextView) findViewById(R.id.scenario_description_count);
        EditText descriptionText = (EditText) findViewById(R.id.scenario_description_value);
        Spinner groupList = (Spinner) findViewById(R.id.groupList);

        //limiting description text length
        descriptionText.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_description_chars), descriptionCountText));

        //setting adapter for list of research groups
        researchGroupAdapter = new ResearchGroupAdapter(this, R.layout.base_row_simple, new ArrayList<ResearchGroup>());
        groupList.setAdapter(researchGroupAdapter);
    }

    private void updateData() {
        if (ConnectionUtils.isOnline(this)) {
            (ScenarioAddActivity.service) = (CommonService) new FetchResearchGroups(this, researchGroupAdapter, Values.SERVICE_QUALIFIER_MINE).execute();
        } else {
            showAlert(getString(R.string.error_offline), true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scenario_file_button:
                Intent intent = new Intent(this, FileChooserActivity.class);
                startActivityForResult(intent, Values.SELECT_FILE_FLAG);
                break;
        }

        super.onClick(v);
    }

    @Override
    protected void save() {
        //obtaining layout elements
        Spinner group = (Spinner) findViewById(R.id.groupList);
        EditText scenarioName = (EditText) findViewById(R.id.scenario_name_value);
        EditText description = (EditText) findViewById(R.id.scenario_description_value);
        EditText mime = (EditText) findViewById(R.id.scenario_mime_value);
        TextView fileName = (TextView) findViewById(R.id.fchooserSelectedFile);
        CompoundButton isPrivate = (CompoundButton) findViewById(R.id.scenario_private);

        //creating scenario instance
        Scenario scenario = new Scenario();
        scenario.setScenarioName(scenarioName.getText().toString());
        scenario.setResearchGroupId(group.getSelectedItem() == null ? null : ((ResearchGroup) group.getSelectedItem()).getGroupId());
        scenario.setDescription(description.getText().toString());
        scenario.setMimeType(mime.getText().toString());
        scenario.setFileName(fileName.getText().toString());
        scenario.setPrivate(isPrivate.isChecked());
        scenario.setFilePath(selectedFile);

        validateAndRun(scenario);
    }

    /**
     * Validates if scenario data are correct and starts service for creating scenario if no error found.
     *
     * @param scenario scenario to be created
     */
    private void validateAndRun(Scenario scenario) {

        if (!ConnectionUtils.isOnline(this)) {
            showAlert(getString(R.string.error_offline));
            return;
        }

        StringBuilder error = new StringBuilder();

        //validations
        if (scenario.getResearchGroupId() == null) {
            error.append(getString(R.string.error_no_group_selected)).append('\n');
        }
        if (ValidationUtils.isEmpty(scenario.getScenarioName())) {
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.scenario_name)).append(")").append('\n');
        }
        if (scenario.getFilePath() == null) {
            error.append(getString(R.string.error_no_file_selected)).append('\n');
        }

        //if no error occurs, service starts
        if (error.toString().isEmpty()) {
            service = (CommonService) new CreateScenario(this).execute(scenario);
        } else {
            showAlert(error.toString());
        }
    }

    @Override
    protected void discard() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //obtaining file selected in FileChooserActivity
            case (Values.SELECT_FILE_FLAG): {
                if (resultCode == Activity.RESULT_OK) {
                    selectedFile = data.getExtras().getString(Values.FILE_PATH);
                    TextView selectedFileView = (TextView) findViewById(R.id.fchooserSelectedFile);
                    EditText mimeView = (EditText) findViewById(R.id.scenario_mime_value);
                    TextView fileSizeView = (TextView) findViewById(R.id.scenario_file_size_value);

                    FileSystemResource file = new FileSystemResource(selectedFile);
                    selectedFileView.setText(file.getFilename());
                    mimeView.setText(FileUtils.getMimeType(selectedFile));

                    String fileSize = FileUtils.getFileSize(file.getFile().length());
                    fileSizeView.setText(fileSize);
                    Toast.makeText(this, selectedFile, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
