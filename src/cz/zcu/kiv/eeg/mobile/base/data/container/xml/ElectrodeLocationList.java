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
 * Data container for collection of electrode location data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocations")
public class ElectrodeLocationList implements Parcelable {

    public static final Parcelable.Creator<ElectrodeLocationList> CREATOR
            = new Parcelable.Creator<ElectrodeLocationList>() {
        public ElectrodeLocationList createFromParcel(Parcel in) {
            return new ElectrodeLocationList(in);
        }

        public ElectrodeLocationList[] newArray(int size) {
            return new ElectrodeLocationList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<ElectrodeLocation> electrodeLocations;

    public ElectrodeLocationList() {
    }

    public ElectrodeLocationList(Parcel in) {
        electrodeLocations = new ArrayList<ElectrodeLocation>();
        in.readTypedList(electrodeLocations, ElectrodeLocation.CREATOR);
    }

    public ElectrodeLocationList(List<ElectrodeLocation> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }

    public List<ElectrodeLocation> getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(List<ElectrodeLocation> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }

    public boolean isAvailable() {
        return electrodeLocations != null && !electrodeLocations.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(electrodeLocations);
    }
}
