package cz.zcu.kiv.eeg.mobile.base.ui.reservation;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;

import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class ReservationDetailsFragment extends Fragment {

    public final static String TAG = ReservationDetailsFragment.class.getSimpleName();
    private boolean empty = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null && container != null) {
            empty = false;
            return inflater.inflate(R.layout.reser_details, container, false);
        } else {
            empty = true;
            return inflater.inflate(R.layout.details_empty, container, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        if (!empty) {
            initData();
        }
    }

    private void initData() {

        TextView groupName = (TextView) getActivity().findViewById(R.id.groupValue);
        TextView fromTime = (TextView) getActivity().findViewById(R.id.fromValue);
        TextView toTime = (TextView) getActivity().findViewById(R.id.toValue);
        TextView creatorName = (TextView) getActivity().findViewById(R.id.creatorName);
        TextView creatorMail = (TextView) getActivity().findViewById(R.id.creatorMail);

        Reservation reservation = (Reservation) getArguments().getParcelable("data");
        if (reservation != null) {
            groupName.setText(reservation.getResearchGroup());
            fromTime.setText(reservation.getFromTime());
            toTime.setText(reservation.getToTime());

            creatorName.setText(reservation.getCreatorName());
            creatorMail.setText(reservation.getEmail());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
