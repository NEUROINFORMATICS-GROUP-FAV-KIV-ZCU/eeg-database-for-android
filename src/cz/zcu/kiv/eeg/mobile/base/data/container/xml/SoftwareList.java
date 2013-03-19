package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for holding used software information.
 * Used for XML marshaling and is parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "softwareList")
public class SoftwareList implements Parcelable {

    public static final Parcelable.Creator<SoftwareList> CREATOR
            = new Parcelable.Creator<SoftwareList>() {
        public SoftwareList createFromParcel(Parcel in) {
            return new SoftwareList(in);
        }

        public SoftwareList[] newArray(int size) {
            return new SoftwareList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Software> softwareList;

    public SoftwareList() {
    }

    public SoftwareList(Parcel in) {
        softwareList = new ArrayList<Software>();
        in.readTypedList(softwareList, Software.CREATOR);
    }

    public List<Software> getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(List<Software> softwareList) {
        this.softwareList = softwareList;
    }

    public boolean isAvailable() {
        return softwareList != null && !softwareList.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(softwareList);
    }
}
