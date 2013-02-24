package cz.zcu.kiv.eeg.mobile.base.ui.base.scenario;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.NavigationActivity;
import cz.zcu.kiv.eeg.mobile.base.ui.TabListener;

/**
 * @author Petr Miko
 *         Date: 19.2.13
 */
public class ScenarioActivity extends CommonActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.scenario_list_mine)
                .setTabListener(new TabListener<ListMineScenariosFragment>(
                        this, ListMineScenariosFragment.class.getSimpleName(), ListMineScenariosFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.scenario_list_all)
                .setTabListener(new TabListener<ListAllScenariosFragment>(
                        this, ListAllScenariosFragment.class.getSimpleName(), ListAllScenariosFragment.class));
        actionBar.addTab(tab);

        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scenario_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentActivityIntent = new Intent(this, NavigationActivity.class);
                parentActivityIntent.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(parentActivityIntent);
                finish();
                break;
            case R.id.scenario_add:
                Toast.makeText(this, "Create scenario clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
