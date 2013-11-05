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
package cz.zcu.kiv.eeg.mobile.base.data.container;

import cz.zcu.kiv.eeg.mobile.base.utils.FileUtils;

import java.io.File;
import java.util.Comparator;

/**
 * Decorator for File object used for displaying in file chooser.
 *
 * @author Petr Miko
 */
public class FileInfo extends File {

    private final static Comparator<FileInfo> fileComparator = new Comparator<FileInfo>() {
        @Override
        public int compare(FileInfo lhs, FileInfo rhs) {
            if (lhs.isDirectory() && !rhs.isDirectory()) {
                return -1;
            } else if (!lhs.isDirectory() && rhs.isDirectory()) {
                return 1;
            } else {
                return lhs.getName().compareTo(rhs.getName());
            }
        }
    };
    private String name;

    /**
     * FileInfo constructor.
     *
     * @param file file
     * @param name file name
     */
    public FileInfo(File file, String name) {
        super(file.getAbsolutePath());
        this.name = name;
    }

    /**
     * Getter of file name.
     *
     * @return file name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Getter of file size.
     *
     * @return file size
     */
    public String getFileSize() {
        return FileUtils.getFileSize(length());
    }

    /**
     * Getter of file comparator.
     * Directories are first, files follows.
     * Each directories and files are sorted alphabetically.
     *
     * @return file comparator
     */
    public static Comparator<FileInfo> getFileComparator(){
        return fileComparator;
    }
}
