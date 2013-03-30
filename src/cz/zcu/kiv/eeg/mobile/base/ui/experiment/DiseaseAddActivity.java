package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateDisease;

/**
 * Activity for creating new disease record.
 *
 * @author Petr Miko
 */
public class DiseaseAddActivity extends SaveDiscardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_disease_add);

        initView();
    }

    /**
     * Initializes view elements.
     */
    private void initView() {

        final EditText description = (EditText) findViewById(R.id.disease_add_description);
        TextView descriptionCount = (TextView) findViewById(R.id.disease_add_description_count);
        description.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_description_chars), descriptionCount));
    }

    @Override
    protected void save() {

        Disease record;
        if ((record = getValidRecord()) != null) {
            if (ConnectionUtils.isOnline(this)) {
                new CreateDisease(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, record);
            } else
                showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * Returns valid record or null, if input values are not valid.
     *
     * @return valid record
     */
    private Disease getValidRecord() {

        EditText name = (EditText) findViewById(R.id.disease_add_title);
        EditText description = (EditText) findViewById(R.id.disease_add_description);

        StringBuilder error = new StringBuilder();

        //validations
        if (ValidationUtils.isEmpty(name.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.dialog_title)).append(")").append('\n');
        if (ValidationUtils.isEmpty(description.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.dialog_description)).append(")").append('\n');

        //if no error, run service
        if (error.toString().isEmpty()) {
            Disease record = new Disease();
            record.setName(name.getText().toString());
            record.setDescription(description.getText().toString());
            return record;
        } else {
            showAlert(error.toString());
            return null;
        }
    }

    @Override
    protected void discard() {
        finish();
    }
}
