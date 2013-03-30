package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding collection of used pharmaceutics.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "pharmaceuticals")
public class PharmaceuticalList implements Parcelable {

    public static final Parcelable.Creator<PharmaceuticalList> CREATOR
            = new Parcelable.Creator<PharmaceuticalList>() {
        public PharmaceuticalList createFromParcel(Parcel in) {
            return new PharmaceuticalList(in);
        }

        public PharmaceuticalList[] newArray(int size) {
            return new PharmaceuticalList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Pharmaceutical> pharmaceuticals;

    public PharmaceuticalList() {
    }

    public PharmaceuticalList(Parcel in) {
        pharmaceuticals = new ArrayList<Pharmaceutical>();
        in.readTypedList(pharmaceuticals, Pharmaceutical.CREATOR);
    }

    public PharmaceuticalList(List<Pharmaceutical> pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }

    public List<Pharmaceutical> getPharmaceuticals() {
        return pharmaceuticals;
    }

    public void setPharmaceuticals(List<Pharmaceutical> pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }

    public boolean isAvailable() {
        return pharmaceuticals != null && !pharmaceuticals.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(pharmaceuticals);
    }
}
