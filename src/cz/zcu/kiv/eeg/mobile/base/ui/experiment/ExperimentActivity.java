package cz.zcu.kiv.eeg.mobile.base.ui.experiment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.SwipeTabsAdapter;
import cz.zcu.kiv.eeg.mobile.base.utils.ConnectionUtils;

/**
 * Activity for displaying experiments.
 * Uses tabs and overwrites default icon with section icon.
 *
 * @author Petr Miko
 */
public class ExperimentActivity extends CommonActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_action_description);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.base_experiment);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        SwipeTabsAdapter swipeTabsAdapter = new SwipeTabsAdapter(this, actionBar, viewPager);
        swipeTabsAdapter.addTab(ListMineExperimentsFragment.NAME, ListMineExperimentsFragment.class, null);
        swipeTabsAdapter.addTab(ListAllExperimentsFragment.NAME, ListAllExperimentsFragment.class, null);

        if (savedInstanceState != null) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tabIndex", 1));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getSupportMenuInflater();
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

                if (ConnectionUtils.isOnline(this)) {
                    Intent addExperimentIntent = new Intent();
                    addExperimentIntent.setClass(this, ExperimentAddActivity.class);
                    startActivity(addExperimentIntent);
                    return true;
                } else
                    showAlert(getString(R.string.error_offline));


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
        outState.putInt("tabIndex", getSupportActionBar().getSelectedNavigationIndex());
    }
}
