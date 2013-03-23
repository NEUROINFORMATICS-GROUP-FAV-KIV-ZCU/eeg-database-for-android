package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Hardware;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Hardware records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class HardwareAdapter extends ArrayAdapter<Hardware> {

    private final Context context;
    private final int resourceId;

    /**
     * Adapter constructor.
     *
     * @param context    context
     * @param resourceId hardware layout identifier
     * @param items      hardware item collection
     */
    public HardwareAdapter(Context context, int resourceId, List<Hardware> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    /**
     * Getter of row view.
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Getter of row view in drop down element (spinner like).
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Getter of row view.
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    private View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Hardware record = getItem(position);
        if (record != null) {
            TextView hwId = (TextView) row.findViewById(R.id.row_hardware_id);
            TextView hwTitle = (TextView) row.findViewById(R.id.row_hardware_title);
            TextView hwType = (TextView) row.findViewById(R.id.row_hardware_type);

            if (hwId != null) {
                hwId.setText(Integer.toString(record.getHardwareId()));
            }
            if (hwTitle != null) {
                hwTitle.setText(record.getTitle());
            }
            if (hwType != null) {
                hwType.setText(record.getType());
            }
        }
        return row;
    }
}
