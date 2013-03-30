package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for collection of hardware information. Supports parcelling and xml marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "hardwareList")
public class HardwareList implements Parcelable {

    public static final Parcelable.Creator<HardwareList> CREATOR
            = new Parcelable.Creator<HardwareList>() {
        public HardwareList createFromParcel(Parcel in) {
            return new HardwareList(in);
        }

        public HardwareList[] newArray(int size) {
            return new HardwareList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Hardware> hardwareList;

    public HardwareList() {
    }

    public HardwareList(Parcel in) {
        hardwareList = new ArrayList<Hardware>();
        in.readTypedList(hardwareList, Hardware.CREATOR);
    }

    public HardwareList(List<Hardware> hardwareList) {
        this.hardwareList = hardwareList;
    }

    public List<Hardware> getHardwareList() {
        return hardwareList;
    }

    public void setHardwareList(List<Hardware> hardwareList) {
        this.hardwareList = hardwareList;
    }

    public boolean isAvailable() {
        return hardwareList != null && !hardwareList.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(hardwareList);
    }
}
