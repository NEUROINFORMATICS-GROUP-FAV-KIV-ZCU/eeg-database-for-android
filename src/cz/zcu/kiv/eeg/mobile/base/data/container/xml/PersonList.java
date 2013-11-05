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
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding list of people/users of eeg base.
 *
 * @author Petr Miko
 */
@Root(name = "people")
public class PersonList implements Parcelable {

    public static final Creator<PersonList> CREATOR
            = new Creator<PersonList>() {
        public PersonList createFromParcel(Parcel in) {
            return new PersonList(in);
        }

        public PersonList[] newArray(int size) {
            return new PersonList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Person> people;

    public PersonList() {
    }

    public PersonList(Parcel in) {
        people = new ArrayList<Person>();
        in.readTypedList(people, Person.CREATOR);
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> Persons) {
        this.people = Persons;
    }

    public boolean isAvailable() {
        return people != null && !people.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(people);
    }
}
