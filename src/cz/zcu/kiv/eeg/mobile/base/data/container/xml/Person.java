package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for person information.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "person")
public class Person implements Parcelable {

    public static final Parcelable.Creator<Person> CREATOR
            = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
    @Element
    private String name;
    @Element
    private String surname;
    @Element
    private String birthday;
    @Element
    private String gender;
    @Element
    private String email;
    @Element(required = false)
    private String leftHanded;
    @Element(required = false)
    private String notes;
    @Element(required = false)
    private String phone;

    public Person() {
    }

    public Person(Parcel in) {
        name = in.readString();
        surname = in.readString();
        birthday = in.readString();
        gender = in.readString();
        email = in.readString();
        leftHanded = in.readString();
        notes = in.readString();
        phone = in.readString();
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLeftHanded() {
        return leftHanded;
    }

    public void setLeftHanded(String leftHanded) {
        this.leftHanded = leftHanded;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(birthday);
        dest.writeString(gender);
        dest.writeString(email);
        dest.writeString(leftHanded);
        dest.writeString(notes);
        dest.writeString(phone);
    }
}
