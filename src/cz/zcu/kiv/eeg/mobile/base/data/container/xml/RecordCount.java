package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Container for holding of available record count.
 *
 * @author Petr Miko
 */
@Root(name = "recordCount")
public class RecordCount {

    @Element
    private int myRecords;
    @Element
    private int publicRecords;

    public int getMyRecords() {
        return myRecords;
    }

    public void setMyRecords(int myRecords) {
        this.myRecords = myRecords;
    }

    public int getPublicRecords() {
        return publicRecords;
    }

    public void setPublicRecords(int publicRecords) {
        this.publicRecords = publicRecords;
    }
}
