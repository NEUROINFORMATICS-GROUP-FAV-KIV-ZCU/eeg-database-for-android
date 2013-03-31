package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Digitization;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateDigitization;

/**
 * Activity for creating new digitization record.
 *
 * @author Petr Miko
 */
public class DigitizationAddActivity extends SaveDiscardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_digitization_add);

        initView();
    }

    /**
     * Initializes view elements.
     */
    private void initView() {

        EditText filter = (EditText) findViewById(R.id.experiment_digitization_filter_add);

        TextView filterCount = (TextView) findViewById(R.id.experiment_digitization_filter_add_count);
        filter.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_artifact), filterCount));
    }

    /**
     * Reads data from fields, if valid proceeds with creating new record on server.
     */
    @Override
    protected void save() {

        Digitization record;
        if ((record = getValidRecord()) != null) {
            if (ConnectionUtils.isOnline(this)) {
                new CreateDigitization(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, record);
            } else
                showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * Returns valid record or null, if input values are not valid.
     *
     * @return valid record
     */
    private Digitization getValidRecord() {

        EditText filter = (EditText) findViewById(R.id.experiment_digitization_filter_add);
        EditText samplingRate = (EditText) findViewById(R.id.experiment_digitization_sampling_rate_add);
        EditText gain = (EditText) findViewById(R.id.experiment_digitization_gain_add);

        StringBuilder error = new StringBuilder();

        //validations
        if (ValidationUtils.isEmpty(filter.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_digitization_filter)).append(")").append('\n');
        if (ValidationUtils.isEmpty(samplingRate.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_digitization_sampling_rate)).append(")").append('\n');
        if (ValidationUtils.isEmpty(gain.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_digitization_gain)).append(")").append('\n');

        //if no error, run service
        if (error.toString().isEmpty()) {
            Digitization record = new Digitization();
            record.setFilter(filter.getText().toString());
            record.setGain(Float.parseFloat(gain.getText().toString()));
            record.setSamplingRate(Float.parseFloat(samplingRate.getText().toString()));

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
