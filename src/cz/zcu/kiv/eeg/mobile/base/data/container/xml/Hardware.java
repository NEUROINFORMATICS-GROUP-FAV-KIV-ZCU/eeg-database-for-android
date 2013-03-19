package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container of hardware information.
 * Allows XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "hardware")
public class Hardware implements Parcelable {

    public static final Parcelable.Creator<Hardware> CREATOR
            = new Parcelable.Creator<Hardware>() {
        public Hardware createFromParcel(Parcel in) {
            return new Hardware(in);
        }

        public Hardware[] newArray(int size) {
            return new Hardware[size];
        }
    };
    @Element
    private int hardwareId;
    @Element
    private String title;
    @Element
    private String type;
    @Element(required = false)
    private String description;
    @Element
    private int defaultNumber;

    public Hardware() {
    }

    public Hardware(Parcel in) {
        hardwareId = in.readInt();
        title = in.readString();
        type = in.readString();
        description = in.readString();
        defaultNumber = in.readInt();
    }

    public int getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(int hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        dest.writeInt(hardwareId);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeInt(defaultNumber);
    }
}
