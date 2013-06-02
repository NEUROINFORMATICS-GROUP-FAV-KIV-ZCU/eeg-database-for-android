package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.db.asynctask.CreateElectrodeFix;

/**
 * Activity for creating new electrode fix record.
 *
 * @author Petr Miko
 */
public class ElectrodeFixAddActivity extends SaveDiscardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_electrode_fix_add);

        initView();
    }

    /**
     * Initializes view elements.
     */
    private void initView() {

        EditText fixDescription = (EditText) findViewById(R.id.electrode_add_fix_description);
        TextView fixDescriptionCount = (TextView) findViewById(R.id.electrode_add_fix_description_count);
        fixDescription.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_description_chars), fixDescriptionCount));
    }

    /**
     * Reads data from fields, if valid proceeds with creating new record on server.
     */
    @Override
    protected void save() {

        ElectrodeFix record;
        if ((record = getValidRecord()) != null) {
            if (ConnectionUtils.isOnline(this)) {
                new CreateElectrodeFix(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, record);
            } else
                showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * Returns valid record or null, if input values are not valid.
     *
     * @return valid record
     */
    private ElectrodeFix getValidRecord() {

        EditText fixTitle = (EditText) findViewById(R.id.electrode_add_fix_title);
        EditText fixDescription = (EditText) findViewById(R.id.electrode_add_fix_description);

        StringBuilder error = new StringBuilder();

        //validations
        if (ValidationUtils.isEmpty(fixTitle.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.dialog_title)).append(")").append('\n');
        if (ValidationUtils.isEmpty(fixDescription.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.dialog_description)).append(")").append('\n');

        //if no error, run service
        if (error.toString().isEmpty()) {
            ElectrodeFix record = new ElectrodeFix();
            record.setTitle(fixTitle.getText().toString());
            record.setDescription(fixDescription.getText().toString());

            return record;
        } else {
            showAlert(error.toString());
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void discard() {
        finish();
    }
}
