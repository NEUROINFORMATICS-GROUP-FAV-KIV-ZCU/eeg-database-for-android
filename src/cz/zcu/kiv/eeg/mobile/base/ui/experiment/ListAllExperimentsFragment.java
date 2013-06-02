package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ExperimentAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.db.asynctask.FetchExperiments;

import java.util.ArrayList;

/**
 * Fragment for listing all public experiments.
 * Supports filtering using string query.
 *
 * @author Petr Miko
 */
public class ListAllExperimentsFragment extends ListFragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private final static String TAG = ListAllExperimentsFragment.class.getSimpleName();
    private static ExperimentAdapter adapter;
    private boolean isDualView;
    private int cursorPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            cursorPosition = savedInstanceState.getInt("cursorPos", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_experiment_list, container, false);
        View detailsFrame = view.findViewById(R.id.details);
        isDualView = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        View emptyView = view.findViewById(android.R.id.empty);
        emptyView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListAdapter(null);
        setListAdapter(getAdapter());

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        if (isDualView) {
            listView.setSelector(R.drawable.list_selector);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(cursorPosition);
            setSelection(cursorPosition);
        }

        listView.setTextFilterEnabled(true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exp_refresh:
                update();
                Log.d(TAG, "Refresh data button pressed");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.empty:
                update();
                break;
        }
    }

    /**
     * If online, fetches all public experiments from server.
     */
    private void update() {

        CommonActivity activity = (CommonActivity) getActivity();
        if (ConnectionUtils.isOnline(activity)) {
            new FetchExperiments(activity, getAdapter(), Values.SERVICE_QUALIFIER_ALL).execute();
        } else
            activity.showAlert(activity.getString(R.string.error_offline));
    }

    /**
     * Getter of experiment array adapter.
     * If not created yet, new instance is made.
     *
     * @return experiment array adapter.
     */
    private ExperimentAdapter getAdapter() {
        if (adapter == null)
            adapter = new ExperimentAdapter(getActivity(), R.layout.base_experiment_row, new ArrayList<Experiment>());

        return adapter;
    }

    /**
     * Helper function to show the details of a selected item, either by displaying a fragment in-place in the current UI, or starting a whole new
     * activity in which it is displayed.
     */
    private void showDetails(int index) {
        cursorPosition = index;
        ExperimentAdapter dataAdapter = getAdapter();
        boolean empty = dataAdapter == null || dataAdapter.isEmpty();

        if (isDualView) {
            getListView().setItemChecked(index, true);

            ExperimentDetailsFragment details = new ExperimentDetailsFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if (getFragmentManager().findFragmentByTag(ExperimentDetailsFragment.TAG) == null) {
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }

            Bundle args = new Bundle();
            args.putInt("index", index);
            args.putParcelable("data", empty ? null : dataAdapter.getItem(index));
            details.setArguments(args);

            ft.replace(R.id.details, details, ExperimentDetailsFragment.TAG);
            ft.commit();
        } else if (!empty) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), ExperimentDetailsActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("data", dataAdapter.getItem(index));
            startActivity(intent);
        }
    }

    /**
     * Stores current cursor position on save.
     *
     * @param outState instance state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cursorPos", cursorPosition);
    }

    /**
     * On click displays selected item details.
     *
     * @param listView event list view source (omitted here)
     * @param view     event view source (omitted here)
     * @param position position of selected item in list view
     * @param id       list item identifier
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        showDetails(position);
        this.setSelection(position);
    }

    /**
     * Adds to options menu search possibility.
     *
     * @param menu     menu to extend
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exp_all_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search = menu.findItem(R.id.exp_search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setOnQueryTextListener(this);
        search.setActionView(searchView);
    }

    /**
     * If query is not empty, on submit hides keyboard.
     *
     * @param query filter query
     * @return event handled
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        return true;
    }

    /**
     * Adds filtering after any changes to filter string.
     *
     * @param newText updated filter string
     * @return event handled
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        getAdapter().getFilter().filter(newText);
        return true;
    }
}
