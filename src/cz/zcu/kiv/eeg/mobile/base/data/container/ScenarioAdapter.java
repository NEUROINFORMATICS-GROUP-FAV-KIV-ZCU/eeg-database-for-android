package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Experiment records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class ScenarioAdapter extends ArrayAdapter<Scenario> {

    private final Context context;
    private final int resourceId;

    public ScenarioAdapter(Context context, int resourceId, List<Scenario> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Scenario record = getItem(position);
        if (record != null) {
            TextView scenarioId = (TextView) row.findViewById(R.id.scenarioId);
            TextView scenarioName = (TextView) row.findViewById(R.id.scenarioName);
            TextView scenarioFileName = (TextView) row.findViewById(R.id.scenarioFile);
            TextView scenarioMime = (TextView) row.findViewById(R.id.scenarioMime);

            if (scenarioId != null) {
                scenarioId.setText(record.getScenarioId());
            }
            if (scenarioName != null) {
                scenarioName.setText(record.getScenarioName());
            }
            if (scenarioFileName != null) {
                scenarioFileName.setText(record.getFileName());
            }
            if (scenarioMime != null) {
                scenarioMime.setText(record.getMimeType());
            }
        }
        return row;
    }


}
