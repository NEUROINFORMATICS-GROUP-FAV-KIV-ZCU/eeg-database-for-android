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
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for XM marshaling of electrode fix data.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeFix")
public class ElectrodeFix implements Parcelable {

    public static final Parcelable.Creator<ElectrodeFix> CREATOR
            = new Parcelable.Creator<ElectrodeFix>() {
        public ElectrodeFix createFromParcel(Parcel in) {
            return new ElectrodeFix(in);
        }

        public ElectrodeFix[] newArray(int size) {
            return new ElectrodeFix[size];
        }
    };
    @Element
    private String id;
    @Element
    private String title;
    @Element(required = false)
    private String description;
    @Element
    private int defaultNumber;

    public ElectrodeFix() {
    }

    public ElectrodeFix(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        defaultNumber = in.readInt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(defaultNumber);
    }
}
