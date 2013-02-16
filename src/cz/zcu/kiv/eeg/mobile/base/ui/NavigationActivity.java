package cz.zcu.kiv.eeg.mobile.base.ui;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.base.DashboardFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.base.DataFileUploadFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.AgendaListFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.settings.SettingsActivity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class NavigationActivity extends CommonActivity implements ActionBar.OnNavigationListener {

    private final static String TAG = NavigationActivity.class.getSimpleName();

    private int previousFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base);
        final ActionBar actionBar = getActionBar();
        actionBar.setTitle("");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(actionBar.getThemedContext(), R.array.sections_list, R.layout.base_row_simple);
        actionBar.setListNavigationCallbacks(spinnerAdapter, this);

        if (savedInstanceState != null) {
            previousFragment = savedInstanceState.getInt("previousFragment", 0);
            getActionBar().setSelectedNavigationItem(NavigationActivity.this.previousFragment);
        }
    }


    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment previousFragment = getFragmentManager().findFragmentById(R.id.content);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        switch (itemPosition) {
            case 0:
                DashboardFragment dashboardFrag;
                if (previousFragment == null || !(previousFragment instanceof DashboardFragment)) {
                    dashboardFrag = new DashboardFragment();

                    fragmentTransaction.replace(R.id.content, dashboardFrag, TAG);
                    fragmentTransaction.commit();
                }
                NavigationActivity.this.previousFragment = itemPosition;
                break;
            case 1:
                DataFileUploadFragment dataFileFrag;
                if (previousFragment == null || !(previousFragment instanceof DataFileUploadFragment)) {
                    dataFileFrag = new DataFileUploadFragment();

                    fragmentTransaction.replace(R.id.content, dataFileFrag, TAG);
                    fragmentTransaction.commit();
                }
                NavigationActivity.this.previousFragment = itemPosition;
                break;
            case 2:
                AgendaListFragment agendaFrag;
                if (previousFragment == null || !(previousFragment instanceof AgendaListFragment)) {
                    agendaFrag = new AgendaListFragment();

                    fragmentTransaction.replace(R.id.content, agendaFrag, TAG);
                    fragmentTransaction.commit();
                }

                NavigationActivity.this.previousFragment = itemPosition;
                break;
            case 3:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                getActionBar().setSelectedNavigationItem(NavigationActivity.this.previousFragment);
                return true;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("previousFragment", previousFragment);
    }

}
