package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for collection of electrode location data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocations")
public class ElectrodeLocationList implements Parcelable {

    public static final Parcelable.Creator<ElectrodeLocationList> CREATOR
            = new Parcelable.Creator<ElectrodeLocationList>() {
        public ElectrodeLocationList createFromParcel(Parcel in) {
            return new ElectrodeLocationList(in);
        }

        public ElectrodeLocationList[] newArray(int size) {
            return new ElectrodeLocationList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<ElectrodeLocation> electrodeLocations;

    public ElectrodeLocationList() {
    }

    public ElectrodeLocationList(Parcel in) {
        electrodeLocations = new ArrayList<ElectrodeLocation>();
        in.readTypedList(electrodeLocations, ElectrodeLocation.CREATOR);
    }

    public List<ElectrodeLocation> getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(List<ElectrodeLocation> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }

    public boolean isAvailable() {
        return electrodeLocations != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(electrodeLocations);
    }
}
