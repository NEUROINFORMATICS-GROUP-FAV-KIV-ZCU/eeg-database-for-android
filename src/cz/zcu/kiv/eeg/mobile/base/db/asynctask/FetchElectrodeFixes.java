package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeFixAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeFix;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.*;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching electrodeFixs from eeg base.
 *
 * @author Petr Miko
 */
public class FetchElectrodeFixes extends CommonService<Void, Void, List<ElectrodeFix>> {

    private static final String TAG = FetchElectrodeFixes.class.getSimpleName();
    private final ElectrodeFixAdapter electrodeFixAdapter;

    /**
     * Constructor.
     *
     * @param activity            parent activity
     * @param electrodeFixAdapter adapter for holding collection of electrodeFixes
     */
    public FetchElectrodeFixes(CommonActivity activity, ElectrodeFixAdapter electrodeFixAdapter) {
        super(activity);
        this.electrodeFixAdapter = electrodeFixAdapter;
    }

    /**
     * Method, where all electrodeTypes are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched electrodeFixes
     */
    @Override
    protected List<ElectrodeFix> doInBackground(Void... params) {
        List<?> results = new ArrayList<Object>();
        setState(RUNNING, R.string.working_ws_electrode_fix);

        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.ELECTRODES_FIX.toString());
            results = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<ElectrodeFix>) results;
    }

    /**
     * Fetched records are put into ElectrodeFix adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<ElectrodeFix> resultList) {
        electrodeFixAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ElectrodeFix>() {
                @Override
                public int compare(ElectrodeFix lhs, ElectrodeFix rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (ElectrodeFix electrodeFix : resultList) {
                electrodeFixAdapter.add(electrodeFix);
            }
        }
    }

}
