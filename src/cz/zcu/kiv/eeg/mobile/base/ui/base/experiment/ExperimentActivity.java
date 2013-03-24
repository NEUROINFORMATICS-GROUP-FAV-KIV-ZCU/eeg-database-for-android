package cz.zcu.kiv.eeg.mobile.base.ui.base.experiment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.TabListener;

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
                Intent addExperimentIntent = new Intent();
                addExperimentIntent.setClass(this, ExperimentAddActivity.class);
                startActivity(addExperimentIntent);
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
