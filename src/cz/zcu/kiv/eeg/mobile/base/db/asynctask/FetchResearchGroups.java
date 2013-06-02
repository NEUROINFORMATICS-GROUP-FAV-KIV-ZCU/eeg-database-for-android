package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ResearchGroupAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.ResearchGroup;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.ArrayList;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Service (AsyncTask) for fetching user's research groups from eeg base.
 *
 * @author Petr Miko
 */
public class FetchResearchGroups extends CommonService<Void, Void, List<ResearchGroup>> {

    private static final String TAG = FetchResearchGroups.class.getSimpleName();
    private final ResearchGroupAdapter groupAdapter;
    private final String qualifier;

    /**
     * Constructor.
     *
     * @param activity     parent activity
     * @param groupAdapter adapter where should be stored fetched research groups
     * @param qualifier    qualifier (whether to fetch private or public records)
     */
    public FetchResearchGroups(CommonActivity activity, ResearchGroupAdapter groupAdapter, String qualifier) {
        super(activity);
        this.groupAdapter = groupAdapter;
        this.qualifier = qualifier;
    }

    /**
     * Method, where all research groups are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched research groups
     */
    @Override
    protected List<ResearchGroup> doInBackground(Void... params) {
        setState(RUNNING, R.string.working_ws_groups);
        List<?> results = new ArrayList<Object>();
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.RESEARCH_GROUPS.toString());

            //When any research group does not exist create one for a testing purposes
            if(hash.getAllKeys().size() == 0) {
                ResearchGroup group = new ResearchGroup();
                group.setGroupName("testGroup");
                hash.put("group"+group.hashCode(), group);
            }
            results = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<ResearchGroup>) results;
    }

    /**
     * Fetched research groups are stored into adapter.
     *
     * @param resultList fetched research groups
     */
    @Override
    protected void onPostExecute(List<ResearchGroup> resultList) {
        groupAdapter.clear();
        if (resultList != null && !resultList.isEmpty())
            for (ResearchGroup res : resultList) {
                groupAdapter.add(res);
            }
    }

}
