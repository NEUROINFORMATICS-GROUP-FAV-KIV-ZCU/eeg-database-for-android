package cz.zcu.kiv.eeg.mobile.base.ui.base.dashboard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cz.zcu.kiv.eeg.mobile.base.R;

/**
 * Dashboard fragment, displayed on startup, if user is logged in.
 *
 * @author Petr Miko
 */
public class DashboardFragment extends Fragment {

    public static final String TAG = DashboardFragment.class.getSimpleName();


    /**
     * In moment of creating view inflates dashboard layout.
     *
     * @param inflater           layout inflater
     * @param container          parent view container
     * @param savedInstanceState data from previous fragment instance
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_dashboard, container, false);
    }

}
