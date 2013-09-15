package cz.zcu.kiv.eeg.mobile.base.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeLocation;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing ElectrodeLocation records in ListView.
 *
 * @author Petr Miko
 */
public class ElectrodeLocationAdapter extends ArrayAdapter<ElectrodeLocation> {

    private final Context context;
    private final int resourceId;

    /**
     * Constructor of disease array adapter.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      item collection
     */
    public ElectrodeLocationAdapter(Context context, int resourceId, List<ElectrodeLocation> items) {
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
            LayoutInflater inflater =  LayoutInflater.from(context);
            row = inflater.inflate(resourceId, parent, false);
        }
        ElectrodeLocation record = getItem(position);
        if (record != null) {
            TextView id = (TextView) row.findViewById(R.id.row_electrode_location_id);
            TextView abbr = (TextView) row.findViewById(R.id.row_electrode_location_abbr);
            TextView title = (TextView) row.findViewById(R.id.row_electrode_location_title);
            TextView description = (TextView) row.findViewById(R.id.row_electrode_location_description);

            if (id != null) {
                id.setText(Integer.toString(record.getId()));
            }

            if (abbr != null) {
                abbr.setText(record.getAbbr());
            }
            if (title != null) {
                title.setText(record.getTitle());
            }

            if(description != null){
                description.setText(record.getDescription());
            }
        }
        return row;
    }
}
