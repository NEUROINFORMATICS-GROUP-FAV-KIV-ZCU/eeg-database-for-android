package cz.zcu.kiv.eeg.mobile.base.ui.base.scenario;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.TabListener;

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
                Intent intent = new Intent();
                intent.setClass(this, ScenarioAddActivity.class);
                startActivity(intent);
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
