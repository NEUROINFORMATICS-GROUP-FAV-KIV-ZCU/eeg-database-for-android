/***********************************************************************************************************************
 *
 * This file is part of the eeg-database-for-android project

 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * Petr Ježek, Petr Miko
 *
 **********************************************************************************************************************/
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
    private String personId;
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
        personId = in.readString();
        name = in.readString();
        surname = in.readString();
        gender = in.readString();
        age = in.readInt();
        leftHanded = in.readByte() == Values.TRUE;
        mailUsername = in.readString();
        mailDomain = in.readString();
    }

    public String  getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
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
        dest.writeString(personId);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(gender);
        dest.writeInt(age);
        dest.writeByte(leftHanded ? Values.TRUE : Values.FALSE);
        dest.writeString(mailUsername);
        dest.writeString(mailDomain);
    }
}
