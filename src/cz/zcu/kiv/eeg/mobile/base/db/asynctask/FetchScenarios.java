package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ScenarioAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Scenario;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.DONE;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.ERROR;
import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.RUNNING;

/**
 * Common service (Asynctask) for fetching scenarios from eeg base.
 *
 * @author Petr Miko
 */
public class FetchScenarios extends CommonService<Void, Void, List<Scenario>> {

    private static final String TAG = FetchScenarios.class.getSimpleName();
    private final ScenarioAdapter scenarioAdapter;
    private final String qualifier;

    /**
     * Constructor.
     *
     * @param activity        parent activity
     * @param scenarioAdapter adapter for holding collection of scenarios
     * @param qualifier       qualifier to distinguish whether to fetch private or public data
     */
    public FetchScenarios(CommonActivity activity, ScenarioAdapter scenarioAdapter, String qualifier) {
        super(activity);
        this.scenarioAdapter = scenarioAdapter;
        this.qualifier = qualifier;
    }

    /**
     * Method, where all scenarios are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched scenarios
     */
    @Override
    protected List<Scenario> doInBackground(Void... params) {
        List<Scenario> result = new ArrayList<Scenario>();
        setState(RUNNING, R.string.working_ws_scenarios);
        try {

            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash res = dbSupport.getOrCreateHash("Scenario");
            List<?> scenarios = res.getAllValues();


          result= (List<Scenario>) scenarios;

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
         return result;
    }

    /**
     * Fetched scenarios are put into scenario adapter and sorted.
     *
     * @param resultList fetched scenarios
     */
    @Override
    protected void onPostExecute(List<Scenario> resultList) {
        scenarioAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Scenario>() {
                @Override
                public int compare(Scenario lhs, Scenario rhs) {
                    return lhs.getScenarioId() - rhs.getScenarioId();
                }
            });

            for (Scenario scenario : resultList) {
                scenarioAdapter.add(scenario);
            }
        }
    }

}
