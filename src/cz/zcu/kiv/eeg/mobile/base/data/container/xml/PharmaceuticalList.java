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
 * Data container for holding collection of used pharmaceutics.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "pharmaceuticals")
public class PharmaceuticalList implements Parcelable {

    public static final Parcelable.Creator<PharmaceuticalList> CREATOR
            = new Parcelable.Creator<PharmaceuticalList>() {
        public PharmaceuticalList createFromParcel(Parcel in) {
            return new PharmaceuticalList(in);
        }

        public PharmaceuticalList[] newArray(int size) {
            return new PharmaceuticalList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Pharmaceutical> pharmaceuticals;

    public PharmaceuticalList() {
    }

    public PharmaceuticalList(Parcel in) {
        pharmaceuticals = new ArrayList<Pharmaceutical>();
        in.readTypedList(pharmaceuticals, Pharmaceutical.CREATOR);
    }

    public PharmaceuticalList(List<Pharmaceutical> pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }

    public List<Pharmaceutical> getPharmaceuticals() {
        return pharmaceuticals;
    }

    public void setPharmaceuticals(List<Pharmaceutical> pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }

    public boolean isAvailable() {
        return pharmaceuticals != null && !pharmaceuticals.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(pharmaceuticals);
    }
}
