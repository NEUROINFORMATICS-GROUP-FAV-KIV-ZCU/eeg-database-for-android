package cz.zcu.kiv.eeg.mobile.base.data.container;

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

        long size = length();
        double fSize = size;

        int i = 0;

        for(;size > 1024; i++, size/=1024){
            fSize = size / 1024.0;
        }

        String sSize = String.format("%.2f", fSize);
        switch(i){
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
}
