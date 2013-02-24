package cz.zcu.kiv.eeg.mobile.base.data.container;

import cz.zcu.kiv.eeg.mobile.base.utils.FileUtils;

import java.io.File;

/**
 * Decorator for File object used for displaying in file chooser.
 *
 * @author Petr Miko
 *         Date: 4.2.13
 */
public class FileInfo extends File {

    private String name;

    public FileInfo(File file, String name) {
        super(file.getAbsolutePath());
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getFileSize(){
        return FileUtils.getFileSize(length());
    }
}
