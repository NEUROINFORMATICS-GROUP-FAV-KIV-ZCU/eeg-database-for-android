package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.os.AsyncTask;
import android.os.Bundle;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Weather;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.utils.ValidationUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.CreateWeather;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.TextView;

/**
 * Activity for creating new weather record.
 *
 * @author Petr Miko
 */
public class WeatherAddActivity extends SaveDiscardActivity {

    private int researchGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_experiment_add_weather);


        researchGroupId = getIntent().getIntExtra("groupId", -1);

        if(researchGroupId < 0)
            showAlert(getString(R.string.error_no_group_selected), true);
        else
            initView();
    }

    /**
     * Initializes view elements.
     */
    private void initView() {

        final EditText title = (EditText) findViewById(R.id.experiment_add_weather_title);
        final EditText description = (EditText) findViewById(R.id.experiment_add_weather_description);
        TextView titleCount = (TextView) findViewById(R.id.experiment_add_weather_title_count);
        TextView descriptionCount = (TextView) findViewById(R.id.experiment_add_weather_description_count);
        description.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_weather), descriptionCount));
        title.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_weather), titleCount));
    }

    /**
     * Reads data from fields, if valid proceeds with creating new record on server.
     */
    @Override
    protected void save() {

        Weather record;
        if ((record = getValidRecord()) != null) {
            if (ConnectionUtils.isOnline(this)) {
                new CreateWeather(this, researchGroupId).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, record);
            } else
                showAlert(getString(R.string.error_offline));
        }
    }

    /**
     * Returns valid record or null, if input values are not valid.
     *
     * @return valid record
     */
    private Weather getValidRecord() {

        EditText title = (EditText) findViewById(R.id.experiment_add_weather_title);
        EditText description = (EditText) findViewById(R.id.experiment_add_weather_description);

        StringBuilder error = new StringBuilder();

        //validations
        if (ValidationUtils.isEmpty(title.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_weather_title)).append(")").append('\n');
        if (ValidationUtils.isEmpty(description.getText().toString()))
            error.append(getString(R.string.error_empty_field)).append(" (").append(getString(R.string.experiment_weather_description)).append(")").append('\n');

        //if no error, run service
        if (error.toString().isEmpty()) {
            Weather record = new Weather();
            record.setTitle(title.getText().toString());
            record.setDescription(description.getText().toString());

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
