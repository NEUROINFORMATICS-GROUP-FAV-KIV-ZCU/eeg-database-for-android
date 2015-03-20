package cz.zcu.kiv.eeg.mobile.base.data.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rahul Kadyan, <hi@znck.me>
 */
public interface AdapterInterface {
    View getView(int position, View convertView, ViewGroup parent);

    View getDropDownView(int position, View convertView, ViewGroup parent);
}
