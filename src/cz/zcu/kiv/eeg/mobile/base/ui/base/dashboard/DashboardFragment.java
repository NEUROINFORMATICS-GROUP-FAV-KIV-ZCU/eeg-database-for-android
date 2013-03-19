package cz.zcu.kiv.eeg.mobile.base.ui.base.dashboard;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.ui.base.person.PersonAddActivity;

/**
 * Dashboard fragment, displayed on startup, if user is logged in.
 *
 * @author Petr Miko
 */
public class DashboardFragment extends Fragment {

    public static final String TAG = DashboardFragment.class.getSimpleName();

    /**
     * On create fragment sets, that fragment has own options menu.
     *
     * @param savedInstanceState data from previous fragment instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

    /**
     * Inflates own options menu.
     *
     * @param menu original options menu
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dash_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handler of menu item selected events.
     * @param item menu item event origin
     * @return event handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person_add:
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonAddActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
