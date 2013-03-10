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
 * Custom class of ArrayAdapter. Used for viewing Hardware records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class HardwareAdapter extends ArrayAdapter<Hardware> {

    private final Context context;
    private final int resourceId;

    public HardwareAdapter(Context context, int resourceId, List<Hardware> items) {
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
        Hardware record = getItem(position);
        if (record != null) {
            TextView hwId= (TextView) row.findViewById(R.id.row_hardware_id);
            TextView hwTitle= (TextView) row.findViewById(R.id.row_hardware_title);
            TextView hwType= (TextView) row.findViewById(R.id.row_hardware_type);
            TextView hwDescription= (TextView) row.findViewById(R.id.row_hardware_description);

            if (hwId != null) {
                hwId.setText(Integer.toString(record.getId()));
            }
            if (hwTitle != null) {
                hwTitle.setText(record.getTitle());
            }
            if (hwType != null) {
                hwType.setText(record.getType());
            }
            if (hwDescription != null) {
                hwDescription.setText(record.getDescription());
            }
        }
        return row;
    }
}
