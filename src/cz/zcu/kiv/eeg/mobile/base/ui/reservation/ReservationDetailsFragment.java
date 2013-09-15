package cz.zcu.kiv.eeg.mobile.base.ui.reservation;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.TextView;

/**
 * Fragment for displaying reservation details.
 *
 * @author Petr Miko
 */
public class ReservationDetailsFragment extends Fragment {

    public final static String TAG = ReservationDetailsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Reservation reservation = getArguments() != null ? (Reservation) getArguments().getParcelable("data") : null;
        boolean hasData = reservation != null && getArguments().getInt("index", -1) >= 0;
        if (hasData) {
            View view = inflater.inflate(R.layout.reser_details, container, false);
            initView(view, reservation);
            return view;
        } else {
            return inflater.inflate(R.layout.details_empty, container, false);
        }
    }

    /**
     * Initializes view elements with reservation data.
     *
     * @param view        view to be displayed
     * @param reservation reservation data
     */
    private void initView(View view, Reservation reservation) {

        //obtaining view elements
        TextView groupName = (TextView) view.findViewById(R.id.groupValue);
        TextView fromTime = (TextView) view.findViewById(R.id.fromValue);
        TextView toTime = (TextView) view.findViewById(R.id.toValue);
        TextView creatorName = (TextView) view.findViewById(R.id.creatorName);
        TextView creatorMail = (TextView) view.findViewById(R.id.creatorMail);

        //setting data
        groupName.setText(reservation.getResearchGroup());
        fromTime.setText(reservation.getFromTime().toString());
        toTime.setText(reservation.getToTime().toString());
        creatorName.setText(reservation.getCreatorName());
        creatorMail.setText(reservation.getEmail());
    }

    /**
     * Adds menu items to action bar.
     *
     * @param menu     menu to inflate into
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
