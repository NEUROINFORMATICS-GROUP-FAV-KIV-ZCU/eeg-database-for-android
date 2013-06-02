package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.DiseaseAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Disease;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching diseases from eeg base.
 *
 * @author Petr Miko
 */
public class FetchDiseases extends CommonService<Void, Void, List<Disease>> {

    private static final String TAG = FetchDiseases.class.getSimpleName();
    private final DiseaseAdapter diseaseAdapter;

    /**
     * Constructor.
     *
     * @param activity       parent activity
     * @param diseaseAdapter adapter for holding collection of diseases
     */
    public FetchDiseases(CommonActivity activity, DiseaseAdapter diseaseAdapter) {
        super(activity);
        this.diseaseAdapter = diseaseAdapter;
    }

    /**
     * Method, where all diseases are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched diseases
     */
    @Override
    protected List<Disease> doInBackground(Void... params) {
     setState(RUNNING, R.string.working_ws_disease);
        List<?> result = new ArrayList<Object>();
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.DISEASES.toString());
            result = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Disease>) result;
    }

    /**
     * Fetched records are put into Disease adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Disease> resultList) {
        diseaseAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Disease>() {
                @Override
                public int compare(Disease lhs, Disease rhs) {
                    int sub = lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getDiseaseId() - rhs.getDiseaseId();
                }
            });

            for (Disease disease : resultList) {
                diseaseAdapter.add(disease);
            }
        }
    }

}
