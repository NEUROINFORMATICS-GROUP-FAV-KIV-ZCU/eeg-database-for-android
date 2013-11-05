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
 * Data container for collection of hardware information. Supports parcelling and xml marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "hardwareList")
public class HardwareList implements Parcelable {

    public static final Parcelable.Creator<HardwareList> CREATOR
            = new Parcelable.Creator<HardwareList>() {
        public HardwareList createFromParcel(Parcel in) {
            return new HardwareList(in);
        }

        public HardwareList[] newArray(int size) {
            return new HardwareList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Hardware> hardwareList;

    public HardwareList() {
    }

    public HardwareList(Parcel in) {
        hardwareList = new ArrayList<Hardware>();
        in.readTypedList(hardwareList, Hardware.CREATOR);
    }

    public HardwareList(List<Hardware> hardwareList) {
        this.hardwareList = hardwareList;
    }

    public List<Hardware> getHardwareList() {
        return hardwareList;
    }

    public void setHardwareList(List<Hardware> hardwareList) {
        this.hardwareList = hardwareList;
    }

    public boolean isAvailable() {
        return hardwareList != null && !hardwareList.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(hardwareList);
    }
}
