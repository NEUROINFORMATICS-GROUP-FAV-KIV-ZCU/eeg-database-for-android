package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container of electrode location data for XML marshaling purposes.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocation")
public class ElectrodeLocation implements Parcelable {

    @Element
    private int id;
    @Element
    private String title;
    @Element(required = false)
    private String abbr;
    @Element(required = false)
    private String description;
    @Element(required = false)
    private int defaultNumber;
    @Element
    private ElectrodeFix electrodeFix;
    @Element
    private ElectrodeType electrodeType;

    public ElectrodeLocation(){}

    public ElectrodeLocation(Parcel in){
        id = in.readInt();
        title = in.readString();
        abbr = in.readString();
        description = in.readString();
        defaultNumber = in.readInt();
        electrodeFix = in.readParcelable(ElectrodeFix.class.getClassLoader());
        electrodeType = in.readParcelable(ElectrodeType.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public ElectrodeFix getElectrodeFix() {
        return electrodeFix;
    }

    public void setElectrodeFix(ElectrodeFix electrodeFix) {
        this.electrodeFix = electrodeFix;
    }

    public ElectrodeType getElectrodeType() {
        return electrodeType;
    }

    public void setElectrodeType(ElectrodeType electrodeType) {
        this.electrodeType = electrodeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(abbr);
        dest.writeString(description);
        dest.writeInt(defaultNumber);
        dest.writeParcelable(electrodeFix, flags);
        dest.writeParcelable(electrodeType, flags);
    }

    public static final Parcelable.Creator<ElectrodeLocation> CREATOR
            = new Parcelable.Creator<ElectrodeLocation>() {
        public ElectrodeLocation createFromParcel(Parcel in) {
            return new ElectrodeLocation(in);
        }

        public ElectrodeLocation[] newArray(int size) {
            return new ElectrodeLocation[size];
        }
    };
}
