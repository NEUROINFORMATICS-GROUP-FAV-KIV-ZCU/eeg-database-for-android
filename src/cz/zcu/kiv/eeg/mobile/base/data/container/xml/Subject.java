package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import cz.zcu.kiv.eeg.mobile.base.data.Values;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container of subject information.
 * Supports XML marshaling and is parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "subject")
public class Subject implements Parcelable {

    public static final Parcelable.Creator<Subject> CREATOR
            = new Parcelable.Creator<Subject>() {
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
    @Element
    private int personId;
    @Element
    private String name, surname;
    @Element
    private String gender;
    @Element
    private int age;
    @Element
    private boolean leftHanded;
    @Element(required = false)
    private String mailUsername, mailDomain;

    public Subject() {
    }

    public Subject(Parcel in) {
        personId = in.readInt();
        name = in.readString();
        surname = in.readString();
        gender = in.readString();
        age = in.readInt();
        leftHanded = in.readByte() == Values.TRUE;
        mailUsername = in.readString();
        mailDomain = in.readString();
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGender(char gender) {
        this.gender = String.valueOf(gender);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLeftHanded() {
        return leftHanded;
    }

    public void setLeftHanded(boolean leftHanded) {
        this.leftHanded = leftHanded;
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
        dest.writeInt(personId);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeByte(leftHanded ? Values.TRUE : Values.FALSE);
        dest.writeString(mailUsername);
        dest.writeString(mailDomain);
    }
}
