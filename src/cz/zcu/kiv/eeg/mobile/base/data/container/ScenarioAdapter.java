package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Experiment records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
public class ScenarioAdapter extends ArrayAdapter<Scenario> implements Filterable {

    private final Context context;
    private final int resourceId;
    private List<Scenario> original;
    private List<Scenario> filtered;
    private ScenarioListFilter scenarioListFilter = new ScenarioListFilter();

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

    public void add(Scenario object) {
        original.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        original.clear();
        filtered.clear();
        notifyDataSetChanged();
    }

    @Override
    public Scenario getItem(int position) {
        return filtered.get(position);
    }

    @Override
    public int getCount() {
        return filtered.size();
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return scenarioListFilter;
    }

    private class ScenarioListFilter extends Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            final Filter.FilterResults oReturn = new Filter.FilterResults();
            final List<Scenario> results = new ArrayList<Scenario>();

            if (constraint == null || constraint.toString().isEmpty()) {
                oReturn.values = original;
                oReturn.count = original.size();
            } else {
                if (original != null) {
                    for (Scenario s : original) {
                        if (Integer.toString(s.getScenarioId()).contains(constraint) || s.getScenarioName().toLowerCase().contains(constraint)) {
                            results.add(s);
                        }
                    }
                }
                oReturn.values = results;
                oReturn.count = results.size();
            }
            return oReturn;

        }

        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            filtered = (List<Scenario>) results.values;
            notifyDataSetChanged();
        }
    }
}
