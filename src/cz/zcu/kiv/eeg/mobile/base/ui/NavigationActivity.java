/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eeg.mobile.base.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.AuthenticatorFactory;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.MenuAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.UserProfile;
import cz.zcu.kiv.eeg.mobile.base.localdb.CBDatabase;
import cz.zcu.kiv.eeg.mobile.base.ui.dashboard.DashboardFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.datafile.DataFileUploadFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.experiment.ExperimentActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.reservation.ReservationFragment;
import cz.zcu.kiv.eeg.mobile.base.ui.scenario.ScenarioActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.settings.SettingsActivity;
import cz.zcu.kiv.eeg.mobile.base.utils.Keys;

/**
 * Main application activity.
 * It is responsible for fetching menu items into actionbar as spinner, and also handles menu items onClick events.
 * On menu item click, new activity or fragment is displayed.
 *
 * @author Petr Miko
 */
public class NavigationActivity extends CommonActivity implements ListView.OnItemClickListener, Replication.ChangeListener {
    public String SYNC_URL;
    private CBDatabase db;
    private final static String TAG = NavigationActivity.class.getSimpleName();
    private int previousFragment = -1;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    CharSequence username;
    CharSequence password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base);

        SharedPreferences credentials = getSharedPreferences(Values.PREFS_CREDENTIALS, Context.MODE_PRIVATE);
        username = credentials.getString("username", null);
        password = credentials.getString("password", null);
        SYNC_URL = credentials.getString("url", null);

        db = new CBDatabase(Keys.DB_NAME, NavigationActivity.this);
        startSync();

        db = new CBDatabase(Keys.DB_NAME, NavigationActivity.this);
        UserProfile logged_up =  db.fetchUserProfileData();
        String loggedUserId = logged_up.getUserId();
        String loggedUserName = logged_up.getUserName();

//      String getDefaultResgrpId = db.fetchDefaultResearchGroup(getLoggedUserId);


        SharedPreferences tempData = getSharedPreferences(Values.PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempData.edit();

        if(loggedUserId==null){
            Toast.makeText(NavigationActivity.this,"Error in pulling user profile data",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(NavigationActivity.this,"user id= "+loggedUserId+" userName="+loggedUserName,Toast.LENGTH_LONG).show();
            editor.putString("loggedUserDocID", loggedUserId);
            editor.putString("loggedUserName", loggedUserName);
            editor.commit();
        }

//        if(getDefaultResgrpId==null){
//            Toast.makeText(NavigationActivity.this,"Error in pulling def res grp",Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(NavigationActivity.this,"def res grp id= "+getDefaultResgrpId,Toast.LENGTH_LONG).show();
//            editor.putString("loggedUserDefGrpID", getDefaultResgrpId);
//            editor.commit();
//        }

        Database database = db.getDatabase();
        Toast.makeText(NavigationActivity.this,"doc count= "+database.getDocumentCount(),Toast.LENGTH_LONG).show();

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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(NavigationActivity.this);
        alert.setMessage(NavigationActivity.this.getString(R.string.message_on_exit));
        alert.setNegativeButton(NavigationActivity.this.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.setPositiveButton(NavigationActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                finish();
            }
        });

        if(!NavigationActivity.this.isFinishing()){
            alert.show();
        }

    }


    //Sync Code

    private void startSync() {

        Database database = db.getDatabase();

        URL syncUrl;
        try {
            syncUrl = new URL(SYNC_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Replication pullReplication = database.createPullReplication(syncUrl);
        pullReplication.setContinuous(true);

        Replication pushReplication = database.createPushReplication(syncUrl);
        pushReplication.setContinuous(true);

        Authenticator auth = AuthenticatorFactory.createBasicAuthenticator(username.toString(), password.toString());
        pushReplication.setAuthenticator(auth);
        pullReplication.setAuthenticator(auth);

        pullReplication.start();
        pushReplication.start();

        pullReplication.addChangeListener(this);
        pushReplication.addChangeListener(this);

    }


    @Override
    public void changed(Replication.ChangeEvent event) {

        Replication replication = event.getSource();
        Log.d(TAG, "Replication : " + replication + " changed.");
        if (!replication.isRunning()) {
            String msg = String.format("Replicator %s not running", replication);
            Log.d(TAG, msg);
        }
        else {
            int processed = replication.getCompletedChangesCount();
            int total = replication.getChangesCount();
            String msg = String.format("Replicator processed %d / %d", processed, total);
            Log.d(TAG, msg);
        }

        if (event.getError() != null) {
            showError("Sync error", event.getError());
        }

    }

    public void showError(final String errorMessage, final Throwable throwable) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msg = String.format("%s: %s", errorMessage, throwable);
                Log.e(TAG, msg, throwable);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

    }
}
