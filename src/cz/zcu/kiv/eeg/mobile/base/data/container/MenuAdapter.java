package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;

/**
 * @author Petr Miko
 *         Date: 3.3.13
 */
public class MenuAdapter extends ArrayAdapter<String> {

    private final static String TAG = MenuAdapter.class.getSimpleName();

    private final int resourceId;
    private String[] iconIds;

    public MenuAdapter(Context context, int resourceId, String[] iconsIds, String[] entries) {
        super(context, resourceId, entries);
        this.resourceId = resourceId;
        this.iconIds = iconsIds;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getMenuView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getMenuView(position, convertView, parent);
    }

    public View getMenuView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }

        ImageView iconView = (ImageView) row.findViewById(R.id.menu_icon);
        TextView labelView = (TextView) row.findViewById(R.id.menu_label);

        iconView.setImageResource(row.getResources().getIdentifier(iconIds[position], "drawable", getContext().getPackageName()));
        labelView.setText(getItem(position));

        return row;
    }
}
