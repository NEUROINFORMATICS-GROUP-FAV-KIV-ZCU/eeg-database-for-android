package cz.zcu.kiv.eeg.mobile.base.ws.db.asynctask;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.SSLSimpleClientHttpRequestFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

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
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("username", null);
        String password = credentials.getString("password", null);
        String url = credentials.getString("url", null) + Values.SERVICE_DATAFILE;

        setState(RUNNING, R.string.working_ws_upload_data_file);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        SSLSimpleClientHttpRequestFactory factory = new SSLSimpleClientHttpRequestFactory();
        //so files wont buffer in memory
        factory.setBufferRequestBody(false);
        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());


        try {
            Log.d(TAG, url);
            FileSystemResource toBeUploadedFile = new FileSystemResource(dataFileContents[2]);
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
            form.add("experimentId", dataFileContents[0]);
            form.add("description", dataFileContents[1]);
            form.add("file", toBeUploadedFile);

            HttpEntity<Object> entity = new HttpEntity<Object>(form, requestHeaders);
            // Make the network request
            return restTemplate.postForLocation(url, entity);
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
