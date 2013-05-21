package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.Artifact;
import cz.zcu.kiv.eeg.mobile.base.db.WaspDbSupport;
import net.rehacktive.wasp.WaspDb;
import net.rehacktive.wasp.WaspHash;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for creating new Artifact on eeg base.
 *
 * @author Petr Miko
 */
public class CreateArtifact extends CommonService<Artifact, Void, Artifact> {

    private final static String TAG = CreateArtifact.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public CreateArtifact(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where artifact information is pushed to server in order to create user.
     * All heavy lifting is made here.
     *
     * @param artifacts only one Artifact object is accepted
     * @return information about created artifact
     */
    @Override
    protected Artifact doInBackground(Artifact... artifacts) {


        Artifact artifact = artifacts[0];

        try {
            WaspDb db = new WaspDbSupport().getDb();
            WaspHash hash = db.getHash("Artifact");
            hash.put("hash" + artifact.hashCode(), artifact);

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    /**
     * Informs user whether artifact creation was successful or not.
     *
     * @param artifact returned artifact info if any
     */
    @Override
    protected void onPostExecute(Artifact artifact) {
        if (artifact != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Values.ADD_ARTIFACT_KEY, artifact);
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish();
        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
