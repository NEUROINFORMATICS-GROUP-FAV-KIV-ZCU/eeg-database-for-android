package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ElectrodeTypeAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ElectrodeType;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching electrodeTypes from eeg base.
 *
 * @author Petr Miko
 */
public class FetchElectrodeTypes extends CommonService<Void, Void, List<ElectrodeType>> {

    private static final String TAG = FetchElectrodeTypes.class.getSimpleName();
    private final ElectrodeTypeAdapter electrodeTypeAdapter;

    /**
     * Constructor.
     *
     * @param activity             parent activity
     * @param electrodeTypeAdapter adapter for holding collection of electrodeTypes
     */
    public FetchElectrodeTypes(CommonActivity activity, ElectrodeTypeAdapter electrodeTypeAdapter) {
        super(activity);
        this.electrodeTypeAdapter = electrodeTypeAdapter;
    }

    /**
     * Method, where all electrodeTypes are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched electrodeTypes
     */
    @Override
    protected List<ElectrodeType> doInBackground(Void... params) {
       List<?> results = new ArrayList<Object>();

        setState(RUNNING, R.string.working_ws_electrode_type);
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.ELECTRODE_TYPES.toString());
            if(hash.getAllKeys().size() == 0) {
                ElectrodeType et = new ElectrodeType();
                et.setTitle("testElectrodeType");
                et.setDescription("dummy electrode type for testing purposes");
                hash.put("hash"+et.hashCode(), et);
            }
            results = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<ElectrodeType>) results;
    }

    /**
     * Fetched records are put into ElectrodeType adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<ElectrodeType> resultList) {
        electrodeTypeAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<ElectrodeType>() {
                @Override
                public int compare(ElectrodeType lhs, ElectrodeType rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (ElectrodeType electrodeType : resultList) {
                electrodeTypeAdapter.add(electrodeType);
            }
        }
    }

}
