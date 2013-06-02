package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeLocationAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeLocation;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching electrodeLocations from eeg base.
 *
 * @author Petr Miko
 */
public class FetchElectrodeLocations extends CommonService<Void, Void, List<ElectrodeLocation>> {

    private static final String TAG = FetchElectrodeLocations.class.getSimpleName();
    private final ElectrodeLocationAdapter electrodeLocationAdapter;

    /**
     * Constructor.
     *
     * @param activity                 parent activity
     * @param electrodeLocationAdapter adapter for holding collection of electrodeLocations
     */
    public FetchElectrodeLocations(CommonActivity activity, ElectrodeLocationAdapter electrodeLocationAdapter) {
        super(activity);
        this.electrodeLocationAdapter = electrodeLocationAdapter;
    }

    /**
     * Method, where all electrodeLocations are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched electrodeLocations
     */
    @Override
    protected List<ElectrodeLocation> doInBackground(Void... params) {
        List<?> results = new ArrayList<Object>();

        setState(RUNNING, R.string.working_ws_electrode_location);
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.ELECTRODES_LOC.toString());
            results = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<ElectrodeLocation>) results;
    }

    /**
     * Fetched records are put into ElectrodeLocation adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<ElectrodeLocation> resultList) {
        electrodeLocationAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ElectrodeLocation>() {
                @Override
                public int compare(ElectrodeLocation lhs, ElectrodeLocation rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (ElectrodeLocation electrodeLocation : resultList) {
                electrodeLocationAdapter.add(electrodeLocation);
            }
        }
    }

}
