package cz.zcu.kiv.eeg.mobile.base.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;

/**
 * Adapter for actionbar menu.
 *
 * @author Petr Miko
 */
public class MenuAdapter extends ArrayAdapter<String> {

    private final static String TAG = MenuAdapter.class.getSimpleName();
    private final int resourceId;
    private String[] iconIds;

    /**
     * Menu adapter constructor.
     *
     * @param context    context
     * @param resourceId row layout identifier
     * @param iconsIds   collection of drawable names
     * @param entries    collection of menu item strings
     */
    public MenuAdapter(Context context, int resourceId, String[] iconsIds, String[] entries) {
        super(context, resourceId, entries);
        this.resourceId = resourceId;
        this.iconIds = iconsIds;

    }

    /**
     * Getter of row view in drop down element (spinner like).
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getMenuView(position, convertView, parent);
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
        return getMenuView(position, convertView, parent);
    }

    /**
     * Getter of row view.
     *
     * @param position    position in adapter
     * @param convertView view in which row should be displayed
     * @param parent      parent view
     * @return row view
     */
    public View getMenuView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((CommonActivity) getContext()).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
        }

        ImageView iconView = (ImageView) row.findViewById(R.id.menu_icon);
        TextView labelView = (TextView) row.findViewById(R.id.menu_label);

        iconView.setImageResource(row.getResources().getIdentifier(iconIds[position], "drawable", getContext().getPackageName()));
        labelView.setText(getItem(position));

        return row;
    }
}
