package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.HardwareAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Hardware;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching hardwareList from eeg base.
 *
 * @author Petr Miko
 */
public class FetchHardwareList extends CommonService<Void, Void, List<Hardware>> {

    private static final String TAG = FetchHardwareList.class.getSimpleName();
    private final HardwareAdapter hardwareAdapter;

    /**
     * Constructor.
     *
     * @param activity        parent activity
     * @param hardwareAdapter adapter for holding collection of hardware
     */
    public FetchHardwareList(CommonActivity activity, HardwareAdapter hardwareAdapter) {
        super(activity);
        this.hardwareAdapter = hardwareAdapter;
    }

    /**
     * Method, where all hardwareList are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched hardware
     */
    @Override
    protected List<Hardware> doInBackground(Void... params) {
        List<?> results = new ArrayList<Object>();
        setState(RUNNING, R.string.working_ws_hardware);

        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.HARDWARE.toString());
            results = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Hardware>) results;
    }

    /**
     * Fetched records are put into Hardware adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Hardware> resultList) {
        hardwareAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Hardware>() {
                @Override
                public int compare(Hardware lhs, Hardware rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getHardwareId() - rhs.getHardwareId();
                }
            });

            for (Hardware artifact : resultList) {
                hardwareAdapter.add(artifact);
            }
        }
    }

}
