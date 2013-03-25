package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding list of electrode systems.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeSystems")
public class ElectrodeSystemList implements Parcelable {

    public static final Creator<ElectrodeSystemList> CREATOR
            = new Creator<ElectrodeSystemList>() {
        public ElectrodeSystemList createFromParcel(Parcel in) {
            return new ElectrodeSystemList(in);
        }

        public ElectrodeSystemList[] newArray(int size) {
            return new ElectrodeSystemList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<ElectrodeSystem> electrodeSystems;

    public ElectrodeSystemList() {
    }

    public ElectrodeSystemList(Parcel in) {
        electrodeSystems = new ArrayList<ElectrodeSystem>();
        in.readTypedList(electrodeSystems, ElectrodeSystem.CREATOR);
    }

    public List<ElectrodeSystem> getElectrodeSystems() {
        return electrodeSystems;
    }

    public void setElectrodeSystems(List<ElectrodeSystem> electrodeSystems) {
        this.electrodeSystems = electrodeSystems;
    }

    public boolean isAvailable() {
        return electrodeSystems != null && !electrodeSystems.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(electrodeSystems);
    }
}
