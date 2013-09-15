package cz.zcu.kiv.eeg.mobile.base.ui.scenario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.SwipeTabsAdapter;
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
        ActionBar actionBar = getSupportActionBar();
        boolean recreated = savedInstanceState != null;

        actionBar.setIcon(R.drawable.ic_action_scenario);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.base_scenario);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        SwipeTabsAdapter swipeTabsAdapter = new SwipeTabsAdapter(this, actionBar, viewPager);
        swipeTabsAdapter.addTab(ListMineScenariosFragment.NAME, ListMineScenariosFragment.class, null);
        swipeTabsAdapter.addTab(ListAllScenariosFragment.NAME, ListAllScenariosFragment.class, null);

        if (recreated) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tabIndex", 1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.scenario_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.scenario_add:

                if (ConnectionUtils.isOnline(this)) {
                    Intent intent = new Intent();
                    intent.setClass(this, ScenarioAddActivity.class);
                    startActivity(intent);
                    return true;
                } else
                    showAlert(getString(R.string.error_offline));


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabIndex", getSupportActionBar().getSelectedNavigationIndex());
    }
}
