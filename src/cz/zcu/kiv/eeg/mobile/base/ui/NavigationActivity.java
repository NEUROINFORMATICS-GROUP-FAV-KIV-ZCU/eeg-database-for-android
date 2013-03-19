package cz.zcu.kiv.eeg.mobile.base.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.container.MenuAdapter;
import cz.zcu.kiv.eeg.mobile.base.ui.base.dashboard.DashboardFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.base.datafile.DataFileUploadFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.base.experiment.ExperimentActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.base.scenario.ScenarioActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.ReservationFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.settings.SettingsActivity;

public class NavigationActivity extends CommonActivity implements ActionBar.OnNavigationListener {

    private final static String TAG = NavigationActivity.class.getSimpleName();
    private int previousFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter spinnerAdapter = new MenuAdapter(actionBar.getThemedContext(), R.layout.base_menu_row,
                getResources().getStringArray(R.array.sections_list_icons), getResources().getStringArray(R.array.sections_list));
        actionBar.setListNavigationCallbacks(spinnerAdapter, this);

        if (savedInstanceState != null) {
            previousFragment = savedInstanceState.getInt("previousFragment", 0);
            getActionBar().setSelectedNavigationItem(NavigationActivity.this.previousFragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        Intent intent;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment previousFragment = getFragmentManager().findFragmentById(R.id.content);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        switch (itemPosition) {
            case 0:
                DashboardFragment dashboardFrag;
                if (previousFragment == null || !(previousFragment instanceof DashboardFragment)) {
                    dashboardFrag = new DashboardFragment();

                    fragmentTransaction.replace(R.id.content, dashboardFrag, DashboardFragment.TAG);
                    fragmentTransaction.commit();
                }
                NavigationActivity.this.previousFragment = itemPosition;
                break;
            case 1:
                intent = new Intent();
                intent.setClass(this, ScenarioActivity.class);
                startActivity(intent);
                getActionBar().setSelectedNavigationItem(NavigationActivity.this.previousFragment);
                return true;
            case 2:
                intent = new Intent();
                intent.setClass(this, ExperimentActivity.class);
                startActivity(intent);
                getActionBar().setSelectedNavigationItem(NavigationActivity.this.previousFragment);
                return true;
            case 3:
                DataFileUploadFragment dataFileFrag;
                if (previousFragment == null || !(previousFragment instanceof DataFileUploadFragment)) {
                    dataFileFrag = new DataFileUploadFragment();

                    fragmentTransaction.replace(R.id.content, dataFileFrag, DataFileUploadFragment.TAG);
                    fragmentTransaction.commit();
                }
                NavigationActivity.this.previousFragment = itemPosition;
                break;
            case 4:
                ReservationFragment agendaFrag;
                if (previousFragment == null || !(previousFragment instanceof ReservationFragment)) {
                    agendaFrag = new ReservationFragment();

                    fragmentTransaction.replace(R.id.content, agendaFrag, ReservationFragment.TAG);
                    fragmentTransaction.commit();
                }

                NavigationActivity.this.previousFragment = itemPosition;
                break;
            case 5:
                intent = new Intent();
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
