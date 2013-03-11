package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import cz.zcu.kiv.eeg.mobile.base.ws.data.ReservationData;
import cz.zcu.kiv.eeg.mobile.base.ws.reservation.RemoveReservation;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Custom class of ArrayAdapter. Used for viewing ReservationData records in ListView.
 *
 * @author Petr Miko - miko.petr (at) gmail.com
 */
@SuppressLint("SimpleDateFormat")
public class ReservationAdapter extends ArrayAdapter<Reservation> implements OnClickListener {

    private final static String TAG = ReservationAdapter.class.getSimpleName();
    private final int resourceId, fragmentId;
    private Context context;

    /**
     * Reservation adapter constructor.
     *
     * @param context    context
     * @param fragmentId fragment parent fragment identifier
     * @param resourceId row layout identifier
     * @param items      reservation data collection
     */
    public ReservationAdapter(Context context, int fragmentId, int resourceId, List<Reservation> items) {
        super(context, resourceId, items);
        this.context = context;
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
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        Reservation record = getItem(position);
        if (record != null) {
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            TextView topText = (TextView) row.findViewById(R.id.toptext);
            TextView additionalText = (TextView) row.findViewById(R.id.bottomtext);
            ImageView removeButton = (ImageView) row.findViewById(R.id.removeButton);
            if (topText != null) {
                topText.setText(sf.format(record.getFromTime()) + " – " + sf.format(record.getToTime()));
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

            Log.d(TAG, "Clicked on remove record: " + reservation.getFromTime().toString() + " | " + reservation.getToTime().toString());

            new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).setTitle(context.getString(R.string.reser_dialog_remove_header))
                    .setMessage(context.getString(R.string.reser_dialog_remove_body))
                    .setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ReservationData data = new ReservationData(reservation.getReservationId(), reservation.getResearchGroupId(), reservation.getResearchGroup().toString(),
                                    reservation.getStringFromTime(), reservation.getStringToTime(), reservation.isCanRemove());
                            new RemoveReservation((CommonActivity) context, fragmentId).execute(data);
                        }

                    }).setNegativeButton(context.getString(android.R.string.cancel), null).show();
        }

    }

    /**
     * Context setter.
     *
     * @param context context
     */
    public void setContext(Context context) {
        this.context = context;
    }
}
