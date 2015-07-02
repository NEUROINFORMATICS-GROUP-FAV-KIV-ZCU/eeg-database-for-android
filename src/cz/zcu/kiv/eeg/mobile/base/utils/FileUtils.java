/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
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

        if(url.contains(".")){
            String extension = MimeTypeMap.getFileExtensionFromUrl(
                    url.substring(url.lastIndexOf(".")));
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }else{
            return "";
        }

    }
}
