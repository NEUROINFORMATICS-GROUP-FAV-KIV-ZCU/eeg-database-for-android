package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for disease information.
 * XML marshallable.
 *
 * @author Petr Miko
 */
@Root(name = "disease")
public class Disease implements Parcelable {

    public static final Parcelable.Creator<Disease> CREATOR
            = new Parcelable.Creator<Disease>() {
        public Disease createFromParcel(Parcel in) {
            return new Disease(in);
        }

        public Disease[] newArray(int size) {
            return new Disease[size];
        }
    };
    @Element
    private int diseaseId;
    @Element
    private String name;
    @Element(required = false)
    private String description;

    public Disease() {
    }

    public Disease(Parcel in) {
        diseaseId = in.readInt();
        name = in.readString();
        description = in.readString();
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(diseaseId);
        dest.writeString(name);
        dest.writeString(description);
    }
}
