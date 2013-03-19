package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding list of diseases.
 *
 * @author Petr Miko
 */
@Root(name = "diseases")
public class DiseaseList implements Parcelable {

    public static final Parcelable.Creator<DiseaseList> CREATOR
            = new Parcelable.Creator<DiseaseList>() {
        public DiseaseList createFromParcel(Parcel in) {
            return new DiseaseList(in);
        }

        public DiseaseList[] newArray(int size) {
            return new DiseaseList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Disease> diseases;

    public DiseaseList() {
    }

    public DiseaseList(Parcel in) {
        diseases = new ArrayList<Disease>();
        in.readTypedList(diseases, Disease.CREATOR);
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public boolean isAvailable() {
        return diseases != null && !diseases.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(diseases);
    }
}
