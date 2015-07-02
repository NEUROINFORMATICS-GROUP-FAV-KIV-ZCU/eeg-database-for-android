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
 * Data container of hardware information.
 * Allows XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "hardware")
public class Hardware implements Parcelable {

    public static final Parcelable.Creator<Hardware> CREATOR
            = new Parcelable.Creator<Hardware>() {
        public Hardware createFromParcel(Parcel in) {
            return new Hardware(in);
        }

        public Hardware[] newArray(int size) {
            return new Hardware[size];
        }
    };
    @Element
    private String hardwareId;
    @Element
    private String title;
    @Element
    private String type;
    @Element(required = false)
    private String description;
    @Element
    private int defaultNumber;

    public Hardware() {
    }

    public Hardware(Parcel in) {
        hardwareId = in.readString();
        title = in.readString();
        type = in.readString();
        description = in.readString();
        defaultNumber = in.readInt();
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        dest.writeString(hardwareId);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeInt(defaultNumber);
    }
}
