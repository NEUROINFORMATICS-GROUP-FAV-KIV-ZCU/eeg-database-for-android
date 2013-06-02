package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ExperimentAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Experiment;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching experiments from server.
 *
 * @author Petr Miko
 */
public class FetchExperiments extends CommonService<Void, Void, List<Experiment>> {

    private static final String TAG = FetchExperiments.class.getSimpleName();
    private ExperimentAdapter experimentAdapter;
    private String qualifier;

    /**
     * Constructor.
     *
     * @param activity          parent activity
     * @param experimentAdapter adapter for holding collection of experiments
     * @param qualifier         qualifier to distinguish whether to fetch private or public data
     */
    public FetchExperiments(CommonActivity activity, ExperimentAdapter experimentAdapter, String qualifier) {
        super(activity);
        this.experimentAdapter = experimentAdapter;
        this.qualifier = qualifier;
    }

    /**
     * Method, where all experiments are read from server.
     * All heavy lifting is made here.
     *
     * @param params not used (omitted) here
     * @return list of fetched experiments
     */
    @Override
    protected List<Experiment> doInBackground(Void... params) {
        setState(RUNNING, R.string.working_ws_experiments);
        List<?> results = new ArrayList<Object>();
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.EXPERIMENTS.toString());
            results = hash.getAllValues();

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Experiment>) results;
    }

    /**
     * Read experiments are assigned to adapter here.
     *
     * @param resultList experiments fetched from server
     */
    @Override
    protected void onPostExecute(List<Experiment> resultList) {
        experimentAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Experiment>() {
                @Override
                public int compare(Experiment lhs, Experiment rhs) {
                    return lhs.getExperimentId() - rhs.getExperimentId();
                }
            });

            for (Experiment res : resultList) {
                experimentAdapter.add(res);
            }
        }
    }

}
