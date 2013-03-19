package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for XML marshaling of electrode configuration data.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocation")
public class ElectrodeConf implements Parcelable{

    @Element
    private int id;
    @Element
    private int impedance;
    @Element
    private ElectrodeSystem electrodeSystem;
    @Element(required = false)
    private ElectrodeLocationList electrodeLocations;

    public ElectrodeConf(){}

    public ElectrodeConf(Parcel in){
        id = in.readInt();
        impedance = in.readInt();
        electrodeSystem = in.readParcelable(ElectrodeSystem.class.getClassLoader());
        electrodeLocations = in.readParcelable(ElectrodeLocationList.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImpedance() {
        return impedance;
    }

    public void setImpedance(int impedance) {
        this.impedance = impedance;
    }

    public ElectrodeSystem getElectrodeSystem() {
        return electrodeSystem;
    }

    public void setElectrodeSystem(ElectrodeSystem electrodeSystem) {
        this.electrodeSystem = electrodeSystem;
    }

    public ElectrodeLocationList getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(ElectrodeLocationList electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(impedance);
        dest.writeParcelable(electrodeSystem, flags);
        dest.writeParcelable(electrodeLocations, flags);
    }

    public static final Parcelable.Creator<ElectrodeConf> CREATOR
            = new Parcelable.Creator<ElectrodeConf>() {
        public ElectrodeConf createFromParcel(Parcel in) {
            return new ElectrodeConf(in);
        }

        public ElectrodeConf[] newArray(int size) {
            return new ElectrodeConf[size];
        }
    };
}
