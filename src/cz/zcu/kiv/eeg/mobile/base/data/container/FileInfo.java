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
