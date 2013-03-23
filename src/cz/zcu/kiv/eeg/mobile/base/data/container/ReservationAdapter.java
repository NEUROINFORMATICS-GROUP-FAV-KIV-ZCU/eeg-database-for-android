package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Reservation;
import cz.zcu.kiv.eeg.mobile.base.ws.reservation.RemoveReservation;

import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing Reservation records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
@SuppressLint("SimpleDateFormat")
public class ReservationAdapter extends ArrayAdapter<Reservation> implements OnClickListener {

    private final static String TAG = ReservationAdapter.class.getSimpleName();
    private final int resourceId, fragmentId;
    private CommonActivity parentActivity;

    /**
     * Reservation adapter constructor.
     *
     * @param parentActivity parent CommonActivity
     * @param fragmentId     fragment parent fragment identifier
     * @param resourceId     row layout identifier
     * @param items          reservation data collection
     */
    public ReservationAdapter(CommonActivity parentActivity, int fragmentId, int resourceId, List<Reservation> items) {
        super(parentActivity, resourceId, items);
        this.parentActivity = parentActivity;
        this.resourceId = resourceId;
        this.fragmentId = fragmentId;
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
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = parentActivity.getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Reservation record = getItem(position);
        if (record != null) {
            TextView topText = (TextView) row.findViewById(R.id.toptext);
            TextView additionalText = (TextView) row.findViewById(R.id.bottomtext);
            ImageView removeButton = (ImageView) row.findViewById(R.id.removeButton);
            if (topText != null) {
                topText.setText(record.getFromTime().toTimeString() + " – " + record.getToTime().toTimeString());
            }
            if (additionalText != null) {
                additionalText.setText(record.getResearchGroup());
            }

            if (!record.isCanRemove()) {
                removeButton.setEnabled(false);
                removeButton.setVisibility(View.INVISIBLE);
            } else {
                removeButton.setEnabled(true);
                removeButton.setVisibility(View.VISIBLE);
                removeButton.setTag(record);
                removeButton.setOnClickListener(this);
            }
        }
        return row;
    }

    /**
     * Handling onClick event on row item.
     * Displays dialog, whether user wants to remove reservation.
     *
     * @param v onClick source element
     */
    @Override
    public void onClick(View v) {

        if (v.getTag() instanceof Reservation) {
            final Reservation reservation = (Reservation) v.getTag();

            Log.d(TAG, "Clicked on remove record: " + reservation.getFromTime() + " | " + reservation.getToTime());

            new AlertDialog.Builder(parentActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle(parentActivity.getString(R.string.reser_dialog_remove_header))
                    .setMessage(parentActivity.getString(R.string.reser_dialog_remove_body))
                    .setPositiveButton(parentActivity.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Reservation data = new Reservation(reservation.getReservationId(), reservation.getResearchGroupId(), reservation.getResearchGroup().toString(),
                                    reservation.getFromTime(), reservation.getToTime(), reservation.isCanRemove());
                            new RemoveReservation(parentActivity, fragmentId).execute(data);
                        }

                    }).setNegativeButton(parentActivity.getString(android.R.string.cancel), null).show();
        }

    }

    /**
     * Parent activity setter.
     *
     * @param parentActivity parent CommonActivity
     */
    public void setContext(CommonActivity parentActivity) {
        this.parentActivity = parentActivity;
    }
}
