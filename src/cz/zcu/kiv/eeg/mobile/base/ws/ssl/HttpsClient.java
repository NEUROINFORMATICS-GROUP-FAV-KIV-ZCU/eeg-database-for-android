package cz.zcu.kiv.eeg.mobile.base.ws.ssl;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.security.KeyStore;

/**
 * HTTP client which supports SSL and accepts all certificates.
 * Necessary because eeg base does not have own verified certificate.
 * <p/>
 * Based on existing example, in this implementation is https client a singleton.
 *
 * @author Petr Miko
 * @see <a href="http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https">StackOverflow - Trusting all certificates using HttpClient over HTTPS</a>
 */
public class HttpsClient extends DefaultHttpClient {

    private static HttpsClient instance;

    /**
     * Constructor which uses provided connection manager and sets required http params.
     *
     * @param connectionManager client connection manager
     * @param params            HTTP parameters
     */
    public HttpsClient(ClientConnectionManager connectionManager, HttpParams params) {
        super(connectionManager, params);
    }

    /**
     * Default constructor.
     */
    public HttpsClient() {
        super();
    }

    /**
     * Getter of SSL HTTP client, which accepts all certificates.
     * If such client could not be created, default HTTP client is returned.
     *
     * @return HTTP+SSL client
     */
    public static HttpsClient getClient() {

        if (instance == null) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory socketFactory = new SpringSSLSocketFactory(trustStore);
                socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
                registry.register(new Scheme("https", socketFactory, 8443));
                ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, registry);
                instance = new HttpsClient(connectionManager, params);
            } catch (Exception e) {
                instance = new HttpsClient();
            }
        }
        return instance;
    }
}