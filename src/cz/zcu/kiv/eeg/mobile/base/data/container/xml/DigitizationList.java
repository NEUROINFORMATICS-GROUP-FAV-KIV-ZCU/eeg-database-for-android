package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding list of digitizations.
 *
 * @author Petr Miko
 */
@Root(name = "digitizations")
public class DigitizationList implements Parcelable {

    public static final Creator<DigitizationList> CREATOR
            = new Creator<DigitizationList>() {
        public DigitizationList createFromParcel(Parcel in) {
            return new DigitizationList(in);
        }

        public DigitizationList[] newArray(int size) {
            return new DigitizationList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Digitization> digitizations;

    public DigitizationList() {
    }

    public DigitizationList(Parcel in) {
        digitizations = new ArrayList<Digitization>();
        in.readTypedList(digitizations, Digitization.CREATOR);
    }

    public List<Digitization> getDigitizations() {
        return digitizations;
    }

    public void setDigitizations(List<Digitization> digitizations) {
        this.digitizations = digitizations;
    }

    public boolean isAvailable() {
        return digitizations != null && !digitizations.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(digitizations);
    }
}
