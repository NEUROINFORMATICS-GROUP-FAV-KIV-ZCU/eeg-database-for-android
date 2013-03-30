package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.SaveDiscardActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeFixAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeTypeAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeType;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.utils.LimitedTextWatcher;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchElectrodeFixes;
import cz.zcu.kiv.eeg.mobile.base.ws.asynctask.FetchElectrodeTypes;

import java.util.ArrayList;

/**
 * Activity for creating new electrode location record.
 *
 * @author Petr Miko
 */
public class ElectrodeLocationAddActivity extends SaveDiscardActivity {

    private static ElectrodeFixAdapter electrodeFixAdapter;
    private static ElectrodeTypeAdapter electrodeTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_electrode_add);

        initView();
        updateData();
    }

    /**
     * Initializes spinners and character counter for description field.
     */
    private void initView() {

        Spinner fixes = (Spinner) findViewById(R.id.electrode_add_fix);
        Spinner types = (Spinner) findViewById(R.id.electrode_add_type);

        fixes.setAdapter(getElectrodeFixAdapter());
        types.setAdapter(getElectrodeTypeAdapter());

        TextView description = (TextView) findViewById(R.id.electrode_add_description);
        TextView descriptionCount = (TextView) findViewById(R.id.electrode_add_description_count);
        description.addTextChangedListener(new LimitedTextWatcher(getResources().getInteger(R.integer.limit_description_chars), descriptionCount));

        ImageButton addFix = (ImageButton) findViewById(R.id.electrode_add_fix_new);
        addFix.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.electrode_add_fix_new:
                Intent intent = new Intent();
                intent.setClass(this, ElectrodeFixAddActivity.class);
                startActivityForResult(intent, Values.ADD_ELECTRODE_FIX);
                break;
            default:
                super.onClick(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Values.ADD_ELECTRODE_FIX && resultCode == Activity.RESULT_OK) {
            ElectrodeFix fix = (ElectrodeFix) data.getExtras().get(Values.ADD_ELECTRODE_FIX_KEY);
            electrodeFixAdapter.add(fix);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Fetches data from server, if not already loaded or currently fetching.
     */
    private void updateData() {
        if (!isWorking()) {
            if (getElectrodeFixAdapter().isEmpty())
                updateElectrodeFixes();
            if (getElectrodeTypeAdapter().isEmpty())
                updateElectrodeTypes();
        }
    }

    @Override
    protected void save() {
        //not implemented yet
    }

    @Override
    protected void discard() {
        finish();
    }

    /**
     * Method for fetching electrode fixes from server.
     * If not online, shows error dialog.
     */
    private void updateElectrodeFixes() {
        if (ConnectionUtils.isOnline(this))
            new FetchElectrodeFixes(this, getElectrodeFixAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    /**
     * Method for fetching electrode types from server.
     * If not online, shows error dialog.
     */
    private void updateElectrodeTypes() {
        if (ConnectionUtils.isOnline(this))
            new FetchElectrodeTypes(this, getElectrodeTypeAdapter()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        else
            showAlert(getString(R.string.error_offline));
    }

    /**
     * Getter of electrode fix adapter. If null, creates new.
     *
     * @return electrode fix adapter
     */
    private ElectrodeFixAdapter getElectrodeFixAdapter() {
        if (electrodeFixAdapter == null)
            electrodeFixAdapter = new ElectrodeFixAdapter(this, R.layout.base_electrode_simple_row, new ArrayList<ElectrodeFix>());
        return electrodeFixAdapter;
    }

    /**
     * Getter of electrode type adapter. If null, creates new.
     *
     * @return electrode type adapter
     */
    private ElectrodeTypeAdapter getElectrodeTypeAdapter() {
        if (electrodeTypeAdapter == null)
            electrodeTypeAdapter = new ElectrodeTypeAdapter(this, R.layout.base_electrode_simple_row, new ArrayList<ElectrodeType>());
        return electrodeTypeAdapter;
    }

}
