package cz.zcu.kiv.eeg.mobile.base.ui.base.scenario;

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
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.Scenario;
import cz.zcu.kiv.eeg.mobile.base.data.container.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.eegbase.FetchScenarios;

import java.util.ArrayList;

/**
 * @author Petr Miko
 *         Date: 19.2.13
 */
public class ListAllScenariosFragment extends ListFragment implements SearchView.OnQueryTextListener {

    private final static String TAG = ListAllScenariosFragment.class.getSimpleName();
    private static ScenarioAdapter adapter;
    private boolean isDualView;
    private int cursorPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            cursorPosition = savedInstanceState.getInt("cursorAllPosition", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_scenario_list, container, false);
        View detailsFrame = view.findViewById(R.id.details);
        isDualView = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
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
        }
        listView.setTextFilterEnabled(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isDualView) {
            showDetails(cursorPosition);
            setSelection(cursorPosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scenario_refresh:
                update();
                Log.d(TAG, "Refresh data button pressed");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void update() {

        CommonActivity activity = (CommonActivity) getActivity();
        if (ConnectionUtils.isOnline(activity)) {
            (ScenarioActivity.service) = (CommonService) new FetchScenarios(activity, getAdapter(), Values.SERVICE_QUALIFIER_ALL).execute();
        } else
            activity.showAlert(activity.getString(R.string.error_offline));
    }

    public ScenarioAdapter getAdapter() {
        if (adapter == null) {
            adapter = new ScenarioAdapter(getActivity(), R.layout.base_scenario_row, new ArrayList<Scenario>());
        }
        return adapter;
    }

    /**
     * Helper function to show the details of a selected item, either by displaying a fragment in-place in the current UI, or starting a whole new
     * activity in which it is displayed.
     */
    void showDetails(int index) {
        cursorPosition = index;

        ScenarioAdapter dataAdapter = getAdapter();
        boolean empty = dataAdapter == null || dataAdapter.isEmpty();

        if (isDualView) {
            getListView().setItemChecked(index, true);

            ScenarioDetailsFragment details = new ScenarioDetailsFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if (getFragmentManager().findFragmentByTag(ScenarioDetailsFragment.TAG) == null) {
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }

            Bundle args = new Bundle();
            args.putInt("index", index);
            args.putSerializable("data", empty ? null : dataAdapter.getItem(index));
            details.setArguments(args);

            ft.replace(R.id.details, details, ScenarioDetailsFragment.TAG);
            ft.commit();
        } else if (!empty) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), ScenarioDetailsActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("data", dataAdapter.getItem(index));
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cursorAllPosition", cursorPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        showDetails(pos);
        this.setSelection(pos);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scenario_all_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search = menu.findItem(R.id.scenario_search);
        SearchView searchView = new SearchView(getActivity());
        searchView.setOnQueryTextListener(this);
        search.setActionView(searchView);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getAdapter().getFilter().filter(newText);
        return true;
    }
}
