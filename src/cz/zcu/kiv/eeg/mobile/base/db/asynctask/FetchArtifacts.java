package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.adapter.ArtifactAdapter;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.db.HashConstants;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspHash;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for fetching artifacts from eeg base.
 *
 * @author Petr Miko
 */
public class FetchArtifacts extends CommonService<Void, Void, List<Artifact>> {

    private static final String TAG = FetchArtifacts.class.getSimpleName();
    private final ArtifactAdapter artifactAdapter;

    /**
     * Constructor.
     *
     * @param activity        parent activity
     * @param artifactAdapter adapter for holding collection of artifacts
     */
    public FetchArtifacts(CommonActivity activity, ArtifactAdapter artifactAdapter) {
        super(activity);
        this.artifactAdapter = artifactAdapter;
    }

    /**
     * Method, where all artifacts are read from server.
     * All heavy lifting is made here.
     *
     * @param params omitted here
     * @return list of fetched artifacts
     */
    @Override
    protected List<Artifact> doInBackground(Void... params) {
        List<?> artifacts = null;

        setState(RUNNING, R.string.working_ws_artifacts);
        try {
            WaspDbSupport dbSupport = new WaspDbSupport();
            WaspHash hash = dbSupport.getOrCreateHash(HashConstants.ARTIFACTS.toString());
            artifacts = hash.getAllValues();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return (List<Artifact>) artifacts;
    }


    /**
     * Fetched records are put into Artifact adapter and sorted.
     *
     * @param resultList fetched records
     */
    @Override
    protected void onPostExecute(List<Artifact> resultList) {
        artifactAdapter.clear();
        if (resultList != null && !resultList.isEmpty()) {
            Collections.sort(resultList, new Comparator<Artifact>() {
                @Override
                public int compare(Artifact lhs, Artifact rhs) {
                    return lhs.getCompensation().toLowerCase().compareTo(rhs.getCompensation().toLowerCase());
                }
            });

            for (Artifact artifact : resultList) {
                artifactAdapter.add(artifact);
            }
        }
    }

}
