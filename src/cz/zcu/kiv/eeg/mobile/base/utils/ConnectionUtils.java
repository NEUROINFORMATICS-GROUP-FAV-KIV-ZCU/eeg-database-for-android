package cz.zcu.kiv.eeg.mobile.base.utils;

import android.content.Context;

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
        //Does not need internet connections when an embedded database is used
        return true;
    }

}
