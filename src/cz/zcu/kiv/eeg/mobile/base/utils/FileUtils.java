package cz.zcu.kiv.eeg.mobile.base.utils;

import android.webkit.MimeTypeMap;

/**
 * Method gathering util methods for file handling.
 *
 * @author Petr Miko
 */
public class FileUtils {

    /**
     * Returns file size with size suffix from B up to TB.
     * If is file greater than 1TB, ">1 TB" string will be returned.
     *
     * @param size file length (size in bytes)
     * @return file size with suffix
     */
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

    /**
     * Method returning MIME type, if file suffix is recognized.
     *
     * @param url path to file
     * @return MIME type if is extension recognized. Nothing otherwise.
     */
    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(
                url.substring(url.lastIndexOf(".")));
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
