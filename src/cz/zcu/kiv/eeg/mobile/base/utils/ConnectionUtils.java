package cz.zcu.kiv.eeg.mobile.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Helper class gathering connection util methods.
 *
 * @author Petr Miko
 */
public class ConnectionUtils {

    /**
     * Checks whether device has network connection.
     *
     * @param context context
     * @return true if network is available
     */
    public static boolean isOnline(Context context) {

        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = conMgr.getActiveNetworkInfo();
        return network != null && network.isConnected() && network.isAvailable();
    }

}
