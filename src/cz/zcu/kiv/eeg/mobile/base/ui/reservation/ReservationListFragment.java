package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.container.ReservationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;
import cz.zcu.kiv.eeg.mobile.base.ws.reservation.FetchReservationsToDate;

import java.util.ArrayList;

public class ReservationListFragment extends ListFragment {

    public static final String TAG = ReservationListFragment.class.getSimpleName();

    private boolean isDualView;
    private int cursorPosition;

    private View header = null;
    private static ReservationAdapter dataAdapter = null;

    private final static int HEADER_ROW = 1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if (header != null)
            getListView().addHeaderView(header);

        View detailsFrame = getActivity().findViewById(R.id.details);
        isDualView = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            cursorPosition = savedInstanceState.getInt("cursorPos", 0);
        }

        if (isDualView) {
            getListView().setSelector(R.drawable.list_selector);
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            if (cursorPosition >= HEADER_ROW) {
                showDetails(cursorPosition);
                this.setSelection(cursorPosition);
            }
        }

        setListAdapter(getAdapter());
        dataAdapter.setContext(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        header = inflater.inflate(R.layout.reser_agenda_list_row_header, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        if (pos >= HEADER_ROW && pos <= dataAdapter.getCount()) {
            showDetails(pos);
            this.setSelection(pos);
        } else {
            ReservationDetailsFragment details = (ReservationDetailsFragment) getFragmentManager().findFragmentById(R.id.details);
            if (details != null && details.isVisible()) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.remove(details);
                ft.commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cursorPos", cursorPosition);
    }

    /**
     * Helper function to show the details of a selected item, either by displaying a fragment in-place in the current UI, or starting a whole new
     * activity in which it is displayed.
     */
    void showDetails(int index) {
        cursorPosition = index;

        ReservationAdapter dataAdapter = getAdapter();
        if (dataAdapter != null && !dataAdapter.isEmpty())
            if (isDualView) {
                getListView().setItemChecked(index, true);

                ReservationDetailsFragment oldDetails = (ReservationDetailsFragment) getFragmentManager().findFragmentByTag(ReservationDetailsFragment.TAG);
                ReservationDetailsFragment details = new ReservationDetailsFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (oldDetails == null) {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                } else {
                    ft.detach(oldDetails);
                    ft.remove(oldDetails);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                }
                Bundle args = new Bundle();
                args.putInt("index", index);
                args.putSerializable("data", dataAdapter.getItem(index - HEADER_ROW));
                details.setArguments(args);

                ft.replace(R.id.details, details, ReservationDetailsFragment.TAG);
                ft.commit();

            } else {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ReservationDetailsActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("data", dataAdapter.getItem(index - HEADER_ROW));
                startActivity(intent);
            }
    }

    public void update(int day, int month, int year) {

        CommonActivity activity = (CommonActivity) getActivity();

        if (ConnectionUtils.isOnline(activity)) {
            (ReservationDetailsActivity.service) = (CommonService) new FetchReservationsToDate(activity, getAdapter()).execute(day, month, year);
        } else
            activity.showAlert(activity.getString(R.string.error_offline));
    }

    private ReservationAdapter getAdapter() {
        if (dataAdapter == null)
            dataAdapter = new ReservationAdapter(getActivity(), getId(), R.layout.reser_agenda_list_row, new ArrayList<Reservation>());

        return dataAdapter;
    }

}
