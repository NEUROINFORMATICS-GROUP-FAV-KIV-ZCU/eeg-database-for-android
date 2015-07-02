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
 * Data container for disease information.
 * XML marshallable.
 *
 * @author Petr Miko
 */
@Root(name = "disease")
public class Disease implements Parcelable {

    public static final Parcelable.Creator<Disease> CREATOR
            = new Parcelable.Creator<Disease>() {
        public Disease createFromParcel(Parcel in) {
            return new Disease(in);
        }

        public Disease[] newArray(int size) {
            return new Disease[size];
        }
    };
    @Element
    private String diseaseId;
    @Element
    private String name;
    @Element(required = false)
    private String description;

    public Disease() {
    }

    public Disease(Parcel in) {
        diseaseId = in.readString();
        name = in.readString();
        description = in.readString();
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(diseaseId);
        dest.writeString(name);
        dest.writeString(description);
    }
}
