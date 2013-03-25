package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding list of electrode fix records.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeFixList")
public class ElectrodeFixList implements Parcelable {

    public static final Creator<ElectrodeFixList> CREATOR
            = new Creator<ElectrodeFixList>() {
        public ElectrodeFixList createFromParcel(Parcel in) {
            return new ElectrodeFixList(in);
        }

        public ElectrodeFixList[] newArray(int size) {
            return new ElectrodeFixList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<ElectrodeFix> electrodeFixList;

    public ElectrodeFixList() {
    }

    public ElectrodeFixList(Parcel in) {
        electrodeFixList = new ArrayList<ElectrodeFix>();
        in.readTypedList(electrodeFixList, ElectrodeFix.CREATOR);
    }

    public List<ElectrodeFix> getElectrodeFixList() {
        return electrodeFixList;
    }

    public void setElectrodeFixList(List<ElectrodeFix> electrodeFixList) {
        this.electrodeFixList = electrodeFixList;
    }

    public boolean isAvailable() {
        return electrodeFixList != null && !electrodeFixList.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(electrodeFixList);
    }
}
