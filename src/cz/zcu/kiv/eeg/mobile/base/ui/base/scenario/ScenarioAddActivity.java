package cz.zcu.kiv.eeg.mobile.base.ui.base.scenario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.data.container.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.ui.filechooser.FileChooserActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.FileUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.data.ScenarioData;
import cz.zcu.kiv.eeg.mobile.base.ws.reservation.FetchResearchGroups;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;

/**
 * @author Petr Miko
 *         Date: 25.2.13
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

    private void initView() {

        final TextView descriptionCountText = (TextView) findViewById(R.id.scenario_description_count);

        final TextWatcher datafileDescriptionWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    descriptionCountText.setVisibility(View.VISIBLE);
                    descriptionCountText.setText(getString(R.string.app_characters_left) + (getResources().getInteger(R.integer.limit_description_chars) - s.length()));
                } else {
                    descriptionCountText.setVisibility(View.INVISIBLE);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        EditText descriptionText = (EditText) findViewById(R.id.scenario_description_value);
        descriptionText.addTextChangedListener(datafileDescriptionWatcher);

        researchGroupAdapter = new ResearchGroupAdapter(this, R.layout.base_row_simple, new ArrayList<ResearchGroup>());
        Spinner groupList = (Spinner) findViewById(R.id.groupList);
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
        Spinner group = (Spinner) findViewById(R.id.groupList);
        EditText scenarioName = (EditText) findViewById(R.id.scenario_name_value);
        EditText description = (EditText) findViewById(R.id.scenario_description_value);
        EditText mime = (EditText) findViewById(R.id.scenario_mime_value);
        TextView fileName = (TextView) findViewById(R.id.fchooserSelectedFile);

        ScenarioData scenario = new ScenarioData();
        scenario.setScenarioName(scenarioName.getText().toString());
        scenario.setResearchGroupId(group.getSelectedItem() == null ? null : ((ResearchGroup) group.getSelectedItem()).getResearchGroupId());
        scenario.setDescription(description.getText().toString());
        scenario.setMimeType(mime.getText().toString());
        scenario.setFileName(fileName.getText().toString());
        scenario.setFilePath(selectedFile);

        validateAndRun(scenario);
    }

    private void validateAndRun(ScenarioData scenario) {

        if (!ConnectionUtils.isOnline(this)) {
            showAlert(getString(R.string.error_offline));
            return;
        }

        StringBuilder error = new StringBuilder();

        if (scenario.getResearchGroupId() == null) {
            error.append(getString(R.string.error_no_group_selected)).append('\n');
        }
        if (ValidationUtils.isEmpty(scenario.getScenarioName())) {
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.scenario_name)).append(")").append('\n');
        }
        if (scenario.getFilePath() == null) {
            error.append(getString(R.string.error_no_file_selected)).append('\n');
        }
        if (error.toString().isEmpty()) {
            Toast.makeText(this, "All set, but service to create scenario is not programmed yet. Try after next update!", Toast.LENGTH_SHORT).show();
//            service = (CommonService) new CreatePerson(this).execute(person);
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
