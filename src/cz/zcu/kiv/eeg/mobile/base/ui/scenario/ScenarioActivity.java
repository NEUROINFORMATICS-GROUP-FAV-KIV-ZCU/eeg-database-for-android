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
package cz.zcu.kiv.eeg.mobile.base.ui.scenario;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.TabListener;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;

/**
 * Scenario activity with tabs for choosing between user's or all public scenarios.
 *
 * @author Petr Miko
 */
public class ScenarioActivity extends CommonActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        boolean recreated = savedInstanceState != null;

        actionBar.setIcon(R.drawable.ic_action_scenario);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ActionBar.Tab mine = actionBar.newTab()
                .setText(R.string.scenario_list_mine)
                .setTabListener(new TabListener<ListMineScenariosFragment>(
                        this, ListMineScenariosFragment.class.getSimpleName(), ListMineScenariosFragment.class));
        ActionBar.Tab all = actionBar.newTab()
                .setText(R.string.scenario_list_all)
                .setTabListener(new TabListener<ListAllScenariosFragment>(
                        this, ListAllScenariosFragment.class.getSimpleName(), ListAllScenariosFragment.class));

        actionBar.addTab(mine);
        actionBar.addTab(all);

        if (recreated) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tabIndex", 1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.scenario_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.scenario_add:

//           if (ConnectionUtils.isOnline(this)) {
            Intent intent = new Intent();
            intent.setClass(this, ScenarioAddActivity.class);
            startActivity(intent);
//                } else
//                    showAlert(getString(R.string.error_offline));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabIndex", getActionBar().getSelectedNavigationIndex());
    }
}
