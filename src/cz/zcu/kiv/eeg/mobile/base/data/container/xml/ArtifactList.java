package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for collection of artifact data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "artifacts")
public class ArtifactList implements Parcelable {

    public static final Creator<ArtifactList> CREATOR
            = new Creator<ArtifactList>() {
        public ArtifactList createFromParcel(Parcel in) {
            return new ArtifactList(in);
        }

        public ArtifactList[] newArray(int size) {
            return new ArtifactList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Artifact> artifacts;

    public ArtifactList() {
    }

    public ArtifactList(Parcel in) {
        artifacts = new ArrayList<Artifact>();
        in.readTypedList(artifacts, Artifact.CREATOR);
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public boolean isAvailable() {
        return artifacts != null && !artifacts.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(artifacts);
    }
}
