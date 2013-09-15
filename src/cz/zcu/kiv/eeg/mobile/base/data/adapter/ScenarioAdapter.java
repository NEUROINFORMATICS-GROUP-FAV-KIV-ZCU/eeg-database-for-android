package cz.zcu.kiv.eeg.mobile.base.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Experiment records in ListView.
 * Adapter data can be filtered.
 *
 * @author Petr Miko
 */
public class ScenarioAdapter extends ArrayAdapter<Scenario> implements Filterable {

    private final Context context;
    private final int resourceId;
    private List<Scenario> original;
    private List<Scenario> filtered;
    private ScenarioListFilter scenarioListFilter = new ScenarioListFilter();

    /**
     * Adapter constructor.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      scenario data collection
     */
    public ScenarioAdapter(Context context, int resourceId, List<Scenario> items) {
        super(context, resourceId);
        this.context = context;
        original = new ArrayList<Scenario>(items.size());
        filtered = new ArrayList<Scenario>(items.size());
        for (Scenario s : items) {
            filtered.add(s);
            original.add(s);
        }
        this.resourceId = resourceId;
    }

    /**
     * Add scenario into adapter.
     *
     * @param object scenario object
     */
    public void add(Scenario object) {
        original.add(object);
        filtered.add(object);
        notifyDataSetChanged();
    }

    /**
     * Clears adapter of all records.
     */
    @Override
    public void clear() {
        original.clear();
        filtered.clear();
        notifyDataSetChanged();
    }

    /**
     * Gets scenario from specified row position.
     *
     * @param position position in adapter
     * @return scenario record
     */
    @Override
    public Scenario getItem(int position) {
        return filtered.get(position);
    }

    /**
     * Available scenario objects in adapter.
     *
     * @return scenario count
     */
    @Override
    public int getCount() {
        return filtered.size();
    }

    /**
     * Checker method, whether is adapter empty.
     *
     * @return is adapter empty
     */
    @Override
    public boolean isEmpty() {
        return filtered.isEmpty();
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
            LayoutInflater inflater =  LayoutInflater.from(context);
            row = inflater.inflate(resourceId, parent, false);
        }
        Scenario record = getItem(position);
        if (record != null) {
            TextView scenarioId = (TextView) row.findViewById(R.id.rowScenarioId);
            TextView scenarioName = (TextView) row.findViewById(R.id.rowScenarioName);
            TextView scenarioFileName = (TextView) row.findViewById(R.id.rowScenarioFile);
            TextView scenarioMime = (TextView) row.findViewById(R.id.rowScenarioMime);

            if (scenarioId != null) {
                scenarioId.setText(Integer.toString(record.getScenarioId()));
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

    /**
     * Notifies GUI that adapter content changed.
     * Forces GUI to redraw.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * Getter of data filter.
     *
     * @return filter
     */
    @Override
    public Filter getFilter() {
        return scenarioListFilter;
    }

    /**
     * Filter for scenario adapter.
     */
    private class ScenarioListFilter extends Filter {

        /**
         * Filters data in adapter by input.
         *
         * @param constraint filter string
         * @return filtered data collection
         */
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            final Filter.FilterResults filterResults = new Filter.FilterResults();
            final List<Scenario> results = new ArrayList<Scenario>();

            if (original != null) {
                boolean noConstraint = constraint == null || constraint.toString().trim().isEmpty();
                for (Scenario s : original) {
                    if (noConstraint || Integer.toString(s.getScenarioId()).contains(constraint) || s.getScenarioName().toLowerCase().contains(constraint)) {
                        results.add(s);
                    }
                }
            }
            filterResults.values = results;
            filterResults.count = results.size();
            return filterResults;

        }

        /**
         * Assings filtered data to its collection and forces GUI to update.
         *
         * @param constraint filter string
         * @param results    filtered data collection
         */
        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            filtered = (List<Scenario>) results.values;
            notifyDataSetChanged();
        }
    }
}
