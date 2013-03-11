package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing ResearchGroupData records in
 * ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class ResearchGroupAdapter extends ArrayAdapter<ResearchGroup> {

    private final Context context;
    private final int resourceId;

    /**
     * Research group adapter constructor.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      research group data collection
     */
    public ResearchGroupAdapter(Context context, int resourceId, List<ResearchGroup> items) {
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
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        ResearchGroup record = getItem(position);
        if (record != null) {
            TextView text = (TextView) row;
            if (text != null) {
                text.setText(record.getResearchGroupName());
            }
        }
        return row;
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
        return getView(position, convertView, parent);
    }

}
