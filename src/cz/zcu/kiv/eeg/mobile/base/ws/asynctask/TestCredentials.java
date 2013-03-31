package cz.zcu.kiv.eeg.mobile.base.ws.asynctask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import cz.zcu.kiv.eeg.mobile.base.R;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonActivity;
import cz.zcu.kiv.eeg.mobile.base.archetypes.CommonService;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import cz.zcu.kiv.eeg.mobile.base.data.container.xml.UserInfo;
import cz.zcu.kiv.eeg.mobile.base.ui.NavigationActivity;
import cz.zcu.kiv.eeg.mobile.base.ws.ssl.HttpsClient;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static cz.zcu.kiv.eeg.mobile.base.data.ServiceState.*;

/**
 * Service for testing user's credentials.
 *
 * @author Petr Miko
 */
public class TestCredentials extends CommonService<Void, Void, UserInfo> {

    private final static String TAG = TestCredentials.class.getSimpleName();
    private boolean startupTest;

    /**
     * Constructor method.
     *
     * @param activity    parent activity
     * @param startupTest is this first login (if so, navigation activity will be created on success)
     */
    public TestCredentials(CommonActivity activity, boolean startupTest) {
        super(activity);
        this.startupTest = startupTest;
    }

    @Override
    protected UserInfo doInBackground(Void... params) {
        SharedPreferences credentials = getCredentials();
        String username = credentials.getString("tmp_username", null);
        String password = credentials.getString("tmp_password", null);
        String url = credentials.getString("tmp_url", null) + Values.SERVICE_USER + "login";

        setState(RUNNING, R.string.working_ws_credentials);
        HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<Object> entity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HttpsClient.getClient()));
        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        try {
            // Make the network request
            Log.d(TAG, url);
            ResponseEntity<UserInfo> userInfo = restTemplate.exchange(url, HttpMethod.GET, entity, UserInfo.class);
            return (Values.user = userInfo.getBody());
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            setState(ERROR, e);
            return null;
        } finally {
            setState(DONE);
        }
    }

    @Override
    protected void onPostExecute(UserInfo loggedUser) {
        SharedPreferences credentials = getCredentials();
        if (loggedUser != null) {

            //credentials are correct, save them
            String username = credentials.getString("tmp_username", null);
            String password = credentials.getString("tmp_password", null);
            String url = credentials.getString("tmp_url", null);

            SharedPreferences.Editor editor = credentials.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("url", url);

            editor.commit();

            Toast.makeText(activity, R.string.settings_saved, Toast.LENGTH_SHORT).show();

            if (startupTest)
                activity.startActivity(new Intent(activity, NavigationActivity.class));
            else
                activity.finish();
        }
    }

}
