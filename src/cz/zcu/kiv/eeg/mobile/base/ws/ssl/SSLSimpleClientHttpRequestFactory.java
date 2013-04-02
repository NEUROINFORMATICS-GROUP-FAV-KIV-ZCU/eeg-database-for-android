package cz.zcu.kiv.eeg.mobile.base.ws.ssl;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * SSL HTTP request factory which accepts all SSL certificates.
 * <p/>
 * Based on example from web and previous implementation.
 *
 * @author Petr Miko
 * @see <a href="http://raymondhlee.wordpress.com/2012/07/28/using-spring-resttemplate-to-consume-restful-webservice/">Using Spring RestTemplate to consume restful webservice</a>
 */
public class SSLSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

    @Override
    protected HttpURLConnection openConnection(URL url, Proxy proxy) throws
            IOException {
        final HttpURLConnection httpUrlConnection = super.openConnection(url,
                proxy);
        if (url.getProtocol().toLowerCase().equals("https")) {
            try {

                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{
                        new X509TrustManager() {
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            }

                            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            }

                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[]{};
                            }
                        }
                }, null);
                ((HttpsURLConnection) httpUrlConnection).setSSLSocketFactory(ctx.getSocketFactory());
                ((HttpsURLConnection) httpUrlConnection).setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
        return httpUrlConnection;
    }
}