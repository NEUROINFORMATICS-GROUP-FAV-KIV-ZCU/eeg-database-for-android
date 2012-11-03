package cz.zcu.kiv.eeg.mobile.base.ws.ssl;

import java.security.KeyStore;

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

public class HttpsClient extends DefaultHttpClient {

	private static HttpsClient instance;

	public HttpsClient(ClientConnectionManager ccm, HttpParams params) {
		super(ccm, params);
	}

	public HttpsClient() {
		super();
	}

	public static HttpsClient getClient() {

		if (instance == null) {
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new SpringSSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
				registry.register(new Scheme("https", sf, 8443));
				ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
				instance = new HttpsClient(ccm, params);
			} catch (Exception e) {
				instance = new HttpsClient();
			}
		}
		return instance;
	}
}