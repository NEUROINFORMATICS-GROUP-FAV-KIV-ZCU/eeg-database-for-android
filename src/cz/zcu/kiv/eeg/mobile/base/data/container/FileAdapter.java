package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;

import java.util.List;

/**
 * Adapter for displaying file content in list view.
 *
 * @author Petr Miko
 *         Date: 4.2.13
 */
public class FileAdapter extends ArrayAdapter<FileInfo> {

    private final static String TAG = ReservationAdapter.class.getSimpleName();
    private final Context context;
    private final int resourceId;

    public FileAdapter(Context context, int resourceId, List<FileInfo> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }
        FileInfo record = getItem(position);
        if (record != null) {
            TextView topText = (TextView) row.findViewById(R.id.fileName);
            TextView additionalText = (TextView) row.findViewById(R.id.fileSize);
            if (topText != null) {
                topText.setText(record.getName());
            }
            if (additionalText != null) {
                if (record.isDirectory()) {
                    additionalText.setText("");
                    topText.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_dir,0, 0, 0);
                } else {
                    additionalText.setText(record.getFileSize());
                    topText.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_file,0, 0, 0);
                }
            }
        }
        return row;
    }
}

