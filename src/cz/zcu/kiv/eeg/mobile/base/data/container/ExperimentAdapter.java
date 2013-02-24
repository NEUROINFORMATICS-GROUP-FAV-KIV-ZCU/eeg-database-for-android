package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Experiment records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class ExperimentAdapter extends ArrayAdapter<Experiment> {

    private final static String TAG = ExperimentAdapter.class.getSimpleName();

    private final Context context;
    private final int resourceId;

    public ExperimentAdapter(Context context, int resourceId, List<Experiment> items) {
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
        Experiment record = getItem(position);
        if (record != null) {
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            TextView topText = (TextView) row.findViewById(R.id.toptext);
            TextView additionalText = (TextView) row.findViewById(R.id.bottomtext);

            if (topText != null) {
                topText.setText(record.getExperimentId() + " | " + record.getScenarioName());
            }
            if (additionalText != null) {
                additionalText.setText(sf.format(record.getStartTime()) + " – " + sf.format(record.getEndTime()));
            }
        }
        return row;
    }


}
