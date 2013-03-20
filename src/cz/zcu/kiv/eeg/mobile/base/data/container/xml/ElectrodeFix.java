package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for XM marshaling of electrode fix data.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeFixData")
public class ElectrodeFix implements Parcelable {

    public static final Parcelable.Creator<ElectrodeFix> CREATOR
            = new Parcelable.Creator<ElectrodeFix>() {
        public ElectrodeFix createFromParcel(Parcel in) {
            return new ElectrodeFix(in);
        }

        public ElectrodeFix[] newArray(int size) {
            return new ElectrodeFix[size];
        }
    };
    @Element
    private int id;
    @Element
    private String title;
    @Element(required = false)
    private String description;
    @Element
    private int defaultNumber;

    public ElectrodeFix() {
    }

    public ElectrodeFix(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        defaultNumber = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(defaultNumber);
    }
}
