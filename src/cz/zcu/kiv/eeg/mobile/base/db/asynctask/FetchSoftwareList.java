package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.SoftwareAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Software;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching softwareList from eeg base.
 *
 * @author Petr Miko
 */
public class FetchSoftwareList extends CommonService<Void, Void, List<Software>> {

    private static final String TAG = FetchSoftwareList.class.getSimpleName();
    private final SoftwareAdapter softwareAdapter;

    /**
     * Constructor.
     *
     * @param activity        parent activity
     * @param softwareAdapter adapter for holding collection of software
     */
    public FetchSoftwareList(CommonActivity activity, SoftwareAdapter softwareAdapter) {
        super(activity);
        this.softwareAdapter = softwareAdapter;
    }

    /**
     * Method, where all softwareList are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched software
     */
    @Override
    protected List<Software> doInBackground(Void... params) {
        setState(RUNNING, R.string.working_ws_software);
        List<?> results = new ArrayList<Object>();

        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.SOFTWARE.toString());
            results = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Software>) results;
    }

    /**
     * Fetched records are put into Software adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Software> resultList) {
        softwareAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Software>() {
                @Override
                public int compare(Software lhs, Software rhs) {
                    int sub = lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());

                    if (sub > 0) return 1;
                    else if (sub < 0) return -1;
                    else return lhs.getId() - rhs.getId();
                }
            });

            for (Software artifact : resultList) {
                softwareAdapter.add(artifact);
            }
        }
    }

}
