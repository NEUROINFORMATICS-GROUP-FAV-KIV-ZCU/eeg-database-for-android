package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import java.text.ParseException;

/**
 * Data container for reservation information.
 *
 * @author Petr Miko
 */
@Root(name = "reservation")
public class Reservation implements Parcelable {

    private final static String TAG = Reservation.class.getSimpleName();

    public static final Parcelable.Creator<Reservation> CREATOR
            = new Parcelable.Creator<Reservation>() {
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };
    @Element
    private String researchGroup;
    @Element
    private int researchGroupId;
    @Element
    private int reservationId;
    @Transient
    private TimeContainer fromTime;
    @Transient
    private TimeContainer toTime;
    @Element(required = false)
    private String creatorName;
    @Element(required = false)
    private String creatorMailUsername;
    @Element(required = false)
    private String creatorMailDomain;
    @Element(required = false)
    private boolean canRemove = false;

    public Reservation() {
    }

    public Reservation(Parcel in) {
        reservationId = in.readInt();
        researchGroupId = in.readInt();
        researchGroup = in.readString();
        fromTime = in.readParcelable(TimeContainer.class.getClassLoader());
        toTime = in.readParcelable(TimeContainer.class.getClassLoader());
        creatorName = in.readString();
        creatorMailUsername = in.readString();
        creatorMailDomain = in.readString();
        canRemove = in.readByte() == Values.TRUE;
    }

    public Reservation(int reservationId, int groupId, String groupName, TimeContainer fromTime, TimeContainer toTime, boolean canRemove) {
        this.reservationId = reservationId;
        this.researchGroupId = groupId;
        this.researchGroup = groupName;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.canRemove = canRemove;
    }

    public String getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(String researchGroup) {
        this.researchGroup = researchGroup;
    }

    public int getResearchGroupId() {
        return researchGroupId;
    }

    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public TimeContainer getFromTime() {
        return fromTime;
    }

    public void setFromTime(TimeContainer fromTime) {
        this.fromTime = fromTime;
    }

    @Element(name = "fromTime")
    public void setFromTimeString(String fromTime) {
        try {
            this.fromTime = new TimeContainer(fromTime, "dd.MM.yyyy HH:mm");
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            this.fromTime = null;
        }
    }

    @Element(name = "fromTime")
    public String getFromTimeString() {
        return fromTime.toString();
    }

    public TimeContainer getToTime() {
        return toTime;
    }

    public void setToTime(TimeContainer toTime) {
        this.toTime = toTime;
    }

    @Element(name = "toTime")
    public void setToTimeString(String toTime) {
        try {
            this.toTime = new TimeContainer(toTime, "dd.MM.yyyy HH:mm");
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            this.toTime = null;
        }
    }

    @Element(name = "toTime")
    public String getToTimeString() {
        return toTime.toString();
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorMailUsername() {
        return creatorMailUsername;
    }

    public void setCreatorMailUsername(String creatorMailUsername) {
        this.creatorMailUsername = creatorMailUsername;
    }

    public String getCreatorMailDomain() {
        return creatorMailDomain;
    }

    public void setCreatorMailDomain(String creatorMailDomain) {
        this.creatorMailDomain = creatorMailDomain;
    }

    public boolean isCanRemove() {
        return canRemove;
    }

    public boolean getCanRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }

    @Transient
    public String getEmail() {
        return getCreatorMailUsername() + "@" + getCreatorMailDomain();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reservationId);
        dest.writeInt(reservationId);
        dest.writeString(researchGroup);
        dest.writeParcelable(fromTime, flags);
        dest.writeParcelable(toTime, flags);
        dest.writeString(creatorName);
        dest.writeString(creatorMailUsername);
        dest.writeString(creatorMailDomain);
        dest.writeByte(canRemove ? Values.TRUE : Values.FALSE);
    }
}
