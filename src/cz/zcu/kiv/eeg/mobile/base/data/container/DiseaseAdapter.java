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
 * Custom class of ArrayAdapter. Used for viewing Disease records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class DiseaseAdapter extends ArrayAdapter<Disease> {

    private final Context context;
    private final int resourceId;

    public DiseaseAdapter(Context context, int resourceId, List<Disease> items) {
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
        Disease record = getItem(position);
        if (record != null) {
            TextView diseaseName = (TextView) row.findViewById(R.id.row_disease_name);
            TextView diseaseDescription = (TextView) row.findViewById(R.id.row_disease_description);

            if (diseaseName != null) {
                diseaseName.setText(record.getName());
            }
            if (diseaseDescription != null) {
                diseaseDescription.setText(record.getDescription());
            }
        }
        return row;
    }
}
