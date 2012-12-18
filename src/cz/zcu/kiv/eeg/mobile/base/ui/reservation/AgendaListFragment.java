package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Constants;
import cz.zcu.kiv.eeg.mobile.base.data.container.Reservation;
import cz.zcu.kiv.eeg.mobile.base.data.container.ReservationAdapter;

import java.util.Calendar;

public class AgendaListFragment extends Fragment implements OnClickListener {

    public final static String TAG = AgendaListFragment.class.getSimpleName();

    private static int year = -1, month = -1, day = -1;
    private TextView dateLabel;

    private final OnDateSetListener dateSetListener = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            AgendaListFragment.year = year;
            month = monthOfYear;
            day = dayOfMonth;
            updateDate();
            updateData();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ReservationListFragment list = new ReservationListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.reservation_list, list, ReservationListFragment.TAG);
            ft.commit();
        }
        setHasOptionsMenu(true);
    }

    private void initButtons() {
        Button chooseDateButton = (Button) getActivity().findViewById(R.id.chooseDateButton);
        chooseDateButton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reser_agenda, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        initButtons();
        initData(savedState);
        updateDate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "Saving date...");
        super.onSaveInstanceState(outState);
        outState.putInt("year", year);
        outState.putInt("month", month);
        outState.putInt("day", day);
    }

    private void initData(Bundle savedState) {

        Calendar c = Calendar.getInstance();

        if (savedState != null) {
            year = savedState.getInt("year", c.get(Calendar.YEAR));
            month = savedState.getInt("month", c.get(Calendar.MONTH));
            day = savedState.getInt("day", Calendar.DAY_OF_MONTH);
        } else if (year == -1 && month == -1 && day == -1) {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            if (CommonActivity.service == null)
                updateData();
        }
        dateLabel = (TextView) getActivity().findViewById(R.id.dateLabel);
    }

    protected void updateDate() {
        // Oracle months are counted from zero instead of one
        dateLabel.setText(String.format("%d.%d.%d", day, month + 1, year));
    }

    public void updateData() {
        ReservationListFragment listFragment = (ReservationListFragment) getFragmentManager().findFragmentByTag(ReservationListFragment.TAG);
        if (listFragment != null)
            listFragment.update(day, month + 1, year);
        else
            Log.e(TAG, "Fragment not found!");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.reser_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addReservation:
                Log.d(TAG, "Add new booking time chosen");
                Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                Bundle b = new Bundle();
                b.putInt("year", year);
                b.putInt("month", month);
                b.putInt("day", day);
                intent.putExtras(b);
                startActivityForResult(intent, Constants.ADD_RECORD_FLAG);
                break;
            case R.id.refresh:
                updateData();
                Log.d(TAG, "Refresh data button pressed");
        }
        return super.onOptionsItemSelected(item);
    }

    public void chooseDateClick(View v) {
        Log.d(TAG, "Add new booking time chosen");
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        datePicker.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (Constants.ADD_RECORD_FLAG): {
                if (resultCode == Activity.RESULT_OK) {
                    ReservationListFragment listFragment = (ReservationListFragment) getFragmentManager().findFragmentByTag(ReservationListFragment.TAG);
                    Reservation record = (Reservation) data.getExtras().get(Constants.ADD_RECORD_KEY);
                    ((ReservationAdapter) listFragment.getListAdapter()).add(record);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseDateButton:
                chooseDateClick(v);
                break;
        }

    }

}
