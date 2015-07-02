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
package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.TabListener;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;

/**
 * Activity for displaying experiments.
 * Uses tabs and overwrites default icon with section icon.
 *
 * @author Petr Miko
 */
public class ExperimentActivity extends CommonActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_action_description);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //setting tabs
        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.experiment_list_mine)
                .setTabListener(new TabListener<ListMineExperimentsFragment>(
                        this, ListMineExperimentsFragment.class.getSimpleName(), ListMineExperimentsFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.experiment_list_all)
                .setTabListener(new TabListener<ListAllExperimentsFragment>(
                        this, ListAllExperimentsFragment.class.getSimpleName(), ListAllExperimentsFragment.class));
        actionBar.addTab(tab);

        if (savedInstanceState != null) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tabIndex", 1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exp_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.exp_add:

//                if (ConnectionUtils.isOnline(this)) {
                Intent addExperimentIntent = new Intent();
                addExperimentIntent.setClass(this, ExperimentAddActivity.class);
                startActivity(addExperimentIntent);
//                } else
//                    showAlert(getString(R.string.error_offline));


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On save stores selected tab index.
     *
     * @param outState bundle describing state of current instance
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabIndex", getActionBar().getSelectedNavigationIndex());
    }
}
