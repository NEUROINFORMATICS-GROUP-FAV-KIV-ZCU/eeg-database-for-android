package cz.zcu.kiv.eeg.mobile.base.utils;

import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * @author Petr Miko
 *         Date: 24.2.13
 */
public class FileUtils {

    public static String getFileSize(long size) {
        double fSize = size;

        int i = 0;

        for (; size > 1024; i++, size /= 1024) {
            fSize = size / 1024.0;
        }

        String sSize = String.format("%.2f", fSize);
        switch (i) {
            case 0:
                return sSize + " B";
            case 1:
                return sSize + " KB";
            case 2:
                return sSize + " MB";
            case 3:
                return sSize + " GB";
            default:
                return ">1 TB";
        }
    }

    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(
                url.substring(url.lastIndexOf(".")));
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
