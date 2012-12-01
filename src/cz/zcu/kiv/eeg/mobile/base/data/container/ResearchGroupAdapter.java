package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Custom class of ArrayAdapter. Used for viewing ResearchGroupData records in
 * ListView.
 * 
 * @author Petr Miko - miko.petr (at) gmail.com
 * 
 */
public class ResearchGroupAdapter extends ArrayAdapter<ResearchGroup>{
	
	private final Context context;
	private final int resourceId;

	public ResearchGroupAdapter(Context context, int resourceId, List<ResearchGroup> items) {
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
		ResearchGroup record = getItem(position);
		if (record != null) {
			TextView text = (TextView) row;
			if (text != null) {
				text.setText(record.getResearchGroupName());
			}
		}
		return row;
	}
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
    }

}
