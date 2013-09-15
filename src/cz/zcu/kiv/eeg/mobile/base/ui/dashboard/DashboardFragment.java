package cz.zcu.kiv.eeg.mobile.base.ui.dashboard;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.ActionBar;
import cz.zcu.kiv.eeg.mobile.base.R;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_dashboard);
        actionBar.setIcon(R.drawable.ic_action_person);
    }
}
