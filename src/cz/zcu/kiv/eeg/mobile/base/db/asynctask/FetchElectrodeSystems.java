package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeSystemAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeSystem;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching electrodeSystems from eeg base.
 *
 * @author Petr Miko
 */
public class FetchElectrodeSystems extends CommonService<Void, Void, List<ElectrodeSystem>> {

    private static final String TAG = FetchElectrodeSystems.class.getSimpleName();
    private final ElectrodeSystemAdapter electrodeSystemAdapter;

    /**
     * Constructor.
     *
     * @param activity               parent activity
     * @param electrodeSystemAdapter adapter for holding collection of electrodeSystems
     */
    public FetchElectrodeSystems(CommonActivity activity, ElectrodeSystemAdapter electrodeSystemAdapter) {
        super(activity);
        this.electrodeSystemAdapter = electrodeSystemAdapter;
    }

    /**
     * Method, where all electrodeSystems are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched electrodeSystems
     */
    @Override
    protected List<ElectrodeSystem> doInBackground(Void... params) {
        List<?> results = new ArrayList<Object>();
        setState(RUNNING, R.string.working_ws_electrode_system);
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.ELECTRODE_SYSTEMS.toString());
            results = hash.getAllValues();

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<ElectrodeSystem>) results;
    }

    /**
     * Fetched records are put into ElectrodeSystem adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<ElectrodeSystem> resultList) {
        electrodeSystemAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ElectrodeSystem>() {
                @Override
                public int compare(ElectrodeSystem lhs, ElectrodeSystem rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (ElectrodeSystem electrodeSystem : resultList) {
                electrodeSystemAdapter.add(electrodeSystem);
            }
        }
    }

}
