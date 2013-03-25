package cz.zcu.kiv.eeg.mobile.base.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Artifact records in ListView.
 *
 * @author Petr Miko
 */
public class ArtifactAdapter extends ArrayAdapter<Artifact> {

    private final Context context;
    private final int resourceId;

    /**
     * Constructor of disease array adapter.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      item collection
     */
    public ArtifactAdapter(Context context, int resourceId, List<Artifact> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    /**
     * Getter of row view.
     *
     * @param position    row position
     * @param convertView view, where row should be displayed in
     * @param parent      view parent
     * @return row view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Getter of row view in drop down element (spinner like).
     *
     * @param position    row position, ie. position inside data collection
     * @param convertView view, where row should be displayed in
     * @param parent      view, where row should be displayed in
     * @return row view
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    /**
     * Creates row view using proper layout and data.
     *
     * @param position    row position, ie. position inside data collection
     * @param convertView view, where row should be displayed in
     * @param parent      view, where row should be displayed in
     * @return row view
     */
    private View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Artifact record = getItem(position);
        if (record != null) {
            TextView compensation = (TextView) row.findViewById(R.id.row_artifact_compensation);
            TextView rejectCondition = (TextView) row.findViewById(R.id.row_artifact_reject_condition);

            if (compensation != null) {
                compensation.setText(record.getCompensation());
            }
            if (rejectCondition != null) {
                rejectCondition.setText(record.getRejectCondition());
            }
        }
        return row;
    }
}
