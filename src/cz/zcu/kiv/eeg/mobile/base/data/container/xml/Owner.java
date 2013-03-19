package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Parcelable container of owner information. Supports XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "owner")
public class Owner implements Parcelable {

    public static final Parcelable.Creator<Owner> CREATOR
            = new Parcelable.Creator<Owner>() {
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };
    @Element
    private int id;
    @Element
    private String name;
    @Element
    private String surname;
    @Element(required = false)
    private String mailUsername, mailDomain;

    public Owner() {
    }

    public Owner(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
        mailUsername = in.readString();
        mailDomain = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailDomain() {
        return mailDomain;
    }

    public void setMailDomain(String mailDomain) {
        this.mailDomain = mailDomain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(mailUsername);
        dest.writeString(mailDomain);
    }
}
