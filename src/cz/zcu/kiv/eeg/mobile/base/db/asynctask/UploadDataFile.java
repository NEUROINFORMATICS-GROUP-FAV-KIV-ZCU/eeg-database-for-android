package cz.zcu.kiv.eeg.mobile.base.db.asynctask;

import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Common service (Asynctask) for uploading data file to specified experiment on eeg base.
 *
 * @author Petr Miko
 */
public class UploadDataFile extends CommonService<String, Void, URI> {

    private final static String TAG = UploadDataFile.class.getSimpleName();

    /**
     * Constructor, which sets reference to parent activity.
     *
     * @param context parent activity
     */
    public UploadDataFile(CommonActivity context) {
        super(context);
    }

    /**
     * Method, where data file information is pushed to server in order to create data file record.
     * All heavy lifting is made here.
     *
     * @param dataFileContents must be three params in order - experiment id, description, path to file
     * @return URI of uploaded file
     */
    @Override
    protected URI doInBackground(String... dataFileContents) {


        setState(RUNNING, R.string.working_ws_upload_data_file);



        try {

            FileSystemResource toBeUploadedFile = new FileSystemResource(dataFileContents[2]);
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
            form.add("experimentId", dataFileContents[0]);
            form.add("description", dataFileContents[1]);
            form.add("file", toBeUploadedFile);

            // Make the network request
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
        } finally {
            setState(DONE);
        }
        return null;
    }

    /**
     * If file was successfully uploaded, URI of data file is displayed shortly.
     *
     * @param uri URI to data file on eeg base
     */
    @Override
    protected void onPostExecute(URI uri) {
        if (uri != null) {
            Toast.makeText(activity, R.string.creation_ok, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(activity, R.string.creation_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
