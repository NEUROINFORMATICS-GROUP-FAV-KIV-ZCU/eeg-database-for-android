package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for holding artifact information.
 *
 * @author Petr Miko
 */
@Root(name = "artifact")
public class Artifact implements Parcelable {

    public static final Parcelable.Creator<Artifact> CREATOR
            = new Parcelable.Creator<Artifact>() {
        public Artifact createFromParcel(Parcel in) {
            return new Artifact(in);
        }

        public Artifact[] newArray(int size) {
            return new Artifact[size];
        }
    };

    @Element
    private int artifactId;
    @Element
    private String compensation;
    @Element
    private String rejectCondition;

    public Artifact(){}

    public Artifact(Parcel in) {
        artifactId = in.readInt();
        compensation = in.readString();
        rejectCondition = in.readString();
    }

    public int getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(int artifactId) {
        this.artifactId = artifactId;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    public String getRejectCondition() {
        return rejectCondition;
    }

    public void setRejectCondition(String rejectCondition) {
        this.rejectCondition = rejectCondition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(artifactId);
        dest.writeString(compensation);
        dest.writeString(rejectCondition);
    }
}
