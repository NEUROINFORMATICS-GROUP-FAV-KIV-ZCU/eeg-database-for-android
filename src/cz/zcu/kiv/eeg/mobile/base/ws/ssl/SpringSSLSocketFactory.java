package cz.zcu.kiv.eeg.mobile.base.ws.ssl;

import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Socket factory which accepts all SSL certificates.
 * Necessary, because EEG portal does not provide verified certificate.
 * <p/>
 * Based on existing example.
 *
 * @author Petr Miko
 * @see <a href="http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https">StackOverflow - Trusting all certificates using HttpClient over HTTPS</a>
 */
public class SpringSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance(TLS);

    /**
     * Constructor. Sets dummy trust manager.
     *
     * @param keyStore keystore
     * @throws NoSuchAlgorithmException  error while creating SSL socket factory
     * @throws KeyManagementException    error while creating SSL socket factory
     * @throws KeyStoreException         error while creating SSL socket factory
     * @throws UnrecoverableKeyException error while creating SSL socket factory
     */
    public SpringSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(keyStore);
        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
