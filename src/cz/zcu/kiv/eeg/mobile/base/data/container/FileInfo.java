package cz.zcu.kiv.eeg.mobile.base.data.container;

import cz.zcu.kiv.eeg.mobile.base.utils.FileUtils;

import java.io.File;

/**
 * Decorator for File object used for displaying in file chooser.
 *
 * @author Petr Miko
 */
public class FileInfo extends File {

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
}
