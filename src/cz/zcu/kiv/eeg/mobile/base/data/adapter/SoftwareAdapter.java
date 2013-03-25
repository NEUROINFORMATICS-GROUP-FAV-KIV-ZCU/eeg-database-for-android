package cz.zcu.kiv.eeg.mobile.base.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Software;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Software records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class SoftwareAdapter extends ArrayAdapter<Software> {

    private final Context context;
    private final int resourceId;

    /**
     * Adapter constructor.
     *
     * @param context    context
     * @param resourceId software layout identifier
     * @param items      software item collection
     */
    public SoftwareAdapter(Context context, int resourceId, List<Software> items) {
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
        Software record = getItem(position);
        if (record != null) {
            TextView swId = (TextView) row.findViewById(R.id.row_software_id);
            TextView swTitle = (TextView) row.findViewById(R.id.row_software_title);
            TextView swDescription = (TextView) row.findViewById(R.id.row_software_description);

            if (swId != null) {
                swId.setText(Integer.toString(record.getId()));
            }
            if (swTitle != null) {
                swTitle.setText(record.getTitle());
            }
            if (swDescription != null) {
                swDescription.setText(record.getDescription());
            }
        }
        return row;
    }
}
