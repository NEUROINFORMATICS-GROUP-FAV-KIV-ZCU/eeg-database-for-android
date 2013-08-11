package cz.zcu.kiv.eeg.mobile.base.ui;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.MenuAdapter;
import cz.zcu.kiv.eeg.mobile.base.ui.dashboard.DashboardFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.datafile.DataFileUploadFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.experiment.ExperimentActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.ReservationFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.scenario.ScenarioActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.settings.SettingsActivity;

/**
 * Main application activity.
 * It is responsible for fetching menu items into actionbar as spinner, and also handles menu items onClick events.
 * On menu item click, new activity or fragment is displayed.
 *
 * @author Petr Miko
 */
public class NavigationActivity extends CommonActivity implements ListView.OnItemClickListener {
    private final static String TAG = NavigationActivity.class.getSimpleName();
    private int previousFragment = -1;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base);
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Locate DrawerLayout in drawer_main.xml
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        ListAdapter drawerAdapter = new MenuAdapter(actionBar.getThemedContext(), R.layout.base_menu_row,
                getResources().getStringArray(R.array.sections_list_icons), getResources().getStringArray(R.array.sections_list));
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(this);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.nav_open,
                R.string.nav_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            openSection(0);
        }

        if (savedInstanceState != null) {
            previousFragment = savedInstanceState.getInt("previousFragment", 0);
        }
    }

    public boolean openSection(int itemPosition) {
        Intent intent;

        if (itemPosition != previousFragment) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            switch (itemPosition) {
                // dashboard
                case 0:
                    DashboardFragment dashboardFrag;
                    dashboardFrag = new DashboardFragment();

                    fragmentTransaction.replace(R.id.content, dashboardFrag, DashboardFragment.TAG);
                    fragmentTransaction.commit();
                    previousFragment = itemPosition;
                    break;

                // scenarios
                case 1:
                    intent = new Intent();
                    intent.setClass(this, ScenarioActivity.class);
                    startActivity(intent);
                    break;

                // experiments
                case 2:
                    intent = new Intent();
                    intent.setClass(this, ExperimentActivity.class);
                    startActivity(intent);
                    break;

                // datafile upload
                case 3:
                    DataFileUploadFragment dataFileFrag;
                    dataFileFrag = new DataFileUploadFragment();

                    fragmentTransaction.replace(R.id.content, dataFileFrag, DataFileUploadFragment.TAG);
                    fragmentTransaction.commit();
                    previousFragment = itemPosition;
                    break;

                // reservations
                case 4:
                    ReservationFragment agendaFrag;
                    agendaFrag = new ReservationFragment();

                    fragmentTransaction.replace(R.id.content, agendaFrag, ReservationFragment.TAG);
                    fragmentTransaction.commit();
                    previousFragment = itemPosition;
                    break;

                // application settings
                case 5:
                    intent = new Intent();
                    intent.setClass(this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                default:
                    return false;
            }
        }
        drawerLayout.closeDrawer(drawerList);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("previousFragment", previousFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (drawerLayout.isDrawerOpen(drawerList)) {
                    drawerLayout.closeDrawer(drawerList);
                } else {
                    drawerLayout.openDrawer(drawerList);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        openSection(position);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
