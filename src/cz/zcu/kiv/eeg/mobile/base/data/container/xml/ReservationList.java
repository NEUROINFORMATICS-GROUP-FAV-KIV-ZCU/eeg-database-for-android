package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding reservations collection.
 * Supports parcelable and XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "reservations")
public class ReservationList implements Parcelable {

    public static final Parcelable.Creator<ReservationList> CREATOR
            = new Parcelable.Creator<ReservationList>() {
        public ReservationList createFromParcel(Parcel in) {
            return new ReservationList(in);
        }

        public ReservationList[] newArray(int size) {
            return new ReservationList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Reservation> reservations;

    public ReservationList() {
    }

    public ReservationList(Parcel in) {
        reservations = new ArrayList<Reservation>();
        in.readTypedList(reservations, Reservation.CREATOR);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isAvailable() {
        return reservations != null && !reservations.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(reservations);
    }
}
