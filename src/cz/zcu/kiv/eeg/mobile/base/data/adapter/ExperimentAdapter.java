/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eeg.mobile.base.data.adapter;

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
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Experiment records in ListView.
 * Data can be filtered.
 *
 * @author Petr Miko
 */
public class ExperimentAdapter extends AbstractAdapter<Experiment> implements Filterable {

    private List<Experiment> original;
    private List<Experiment> filtered;
    private ExperimentListFilter experimentListFilter = new ExperimentListFilter();

    /**
     * Experiment adapter constructor.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param items      row data collection
     */
    public ExperimentAdapter(Context context, int resourceId, List<Experiment> items) {
        super(context, resourceId, items);
        original = new ArrayList<Experiment>(items.size());
        filtered = new ArrayList<Experiment>(items.size());
        for (Experiment exp : items) {
            filtered.add(exp);
            original.add(exp);
        }
    }

    /**
     * Adds experiment into adapter.
     *
     * @param object experiment record
     */
    @Override
    public void add(Experiment object) {
        original.add(object);
        filtered.add(object);
        this.notifyDataSetChanged();
    }

    /**
     * Clears adapter of data.
     */
    @Override
    public void clear() {
        original.clear();
        filtered.clear();
        notifyDataSetChanged();
    }

    /**
     * Getter of experiment on specified position.
     *
     * @param position position in adapter
     * @return experiment data
     */
    @Override
    public Experiment getItem(int position) {
        return filtered.get(position);
    }

    /**
     * Available experiment count getter.
     *
     * @return available experiment count
     */
    @Override
    public int getCount() {
        return filtered.size();
    }

    /**
     * Test, whether is adapter empty.
     *
     * @return is adapter empty
     */
    @Override
    public boolean isEmpty() {
        return filtered.isEmpty();
    }

    /**
     * Row view creator method.
     *
     * @param position    position in adapter
     * @param convertView view where row should be displayed in
     * @param parent      view parent
     * @return row view
     */
    protected View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Experiment record = getItem(position);
        if (record != null) {
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            TextView experimentId = (TextView) row.findViewById(R.id.row_experiment_id);
            TextView experimentName = (TextView) row.findViewById(R.id.row_experiment_name);
            TextView experimentTime = (TextView) row.findViewById(R.id.row_experiment_time);

            if (experimentId != null)
                experimentId.setText(Integer.toString(record.getExperimentId()));
            if (experimentName != null)
                experimentName.setText(record.getScenario().getScenarioName());
            if (experimentTime != null)
                experimentTime.setText(record.getStartTime() + " – " + record.getEndTime());
        }
        return row;
    }

    /**
     * Notifies GUI, that data changed.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * Data filter getter.
     *
     * @return filter
     */
    @Override
    public Filter getFilter() {
        return experimentListFilter;
    }

    /**
     * Filter for experiment data.
     */
    private class ExperimentListFilter extends Filter {
        /**
         * Performs filtering upon data collection.
         *
         * @param constraint filter string
         * @return filtered result collection
         */
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            final Filter.FilterResults oReturn = new Filter.FilterResults();
            final List<Experiment> results = new ArrayList<Experiment>();

            if (original != null) {
                boolean noConstraint = constraint == null || constraint.toString().isEmpty();
                for (Experiment exp : original) {
                    if (noConstraint || Integer.toString(exp.getExperimentId()).contains(constraint) || exp.getScenario().getScenarioName().toLowerCase().contains(constraint)) {
                        results.add(exp);
                    }
                }
            }
            oReturn.values = results;
            oReturn.count = results.size();
            return oReturn;

        }

        /**
         * Action on publish results.
         *
         * @param constraint filter string
         * @param results    filtered data
         */
        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            filtered = (List<Experiment>) results.values;
            notifyDataSetChanged();
        }
    }

}
