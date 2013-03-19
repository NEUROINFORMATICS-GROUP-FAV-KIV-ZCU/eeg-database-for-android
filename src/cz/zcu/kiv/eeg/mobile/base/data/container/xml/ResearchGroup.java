package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for Research Group information.
 *
 * @author Petr Miko
 */
@Root(name = "researchGroup")
public class ResearchGroup implements Parcelable {

    public static final Parcelable.Creator<ResearchGroup> CREATOR
            = new Parcelable.Creator<ResearchGroup>() {
        public ResearchGroup createFromParcel(Parcel in) {
            return new ResearchGroup(in);
        }

        public ResearchGroup[] newArray(int size) {
            return new ResearchGroup[size];
        }
    };
    @Element
    private int groupId;
    @Element
    private String groupName;

    public ResearchGroup() {
    }

    public ResearchGroup(Parcel in) {
        groupId = in.readInt();
        groupName = in.readString();
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(groupId);
        dest.writeString(groupName);
    }
}
