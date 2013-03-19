package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for holding information about used pharmaceutics.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "pharmaceutical")
public class Pharmaceutical implements Parcelable {

    public static final Parcelable.Creator<Pharmaceutical> CREATOR
            = new Parcelable.Creator<Pharmaceutical>() {
        public Pharmaceutical createFromParcel(Parcel in) {
            return new Pharmaceutical(in);
        }

        public Pharmaceutical[] newArray(int size) {
            return new Pharmaceutical[size];
        }
    };
    @Element
    private int id;
    @Element
    private String title;
    @Element(required = false)
    private String description;

    public Pharmaceutical() {
    }

    public Pharmaceutical(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
    }
}