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
 * Data container for holding list of electrode systems.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeSystems")
public class ElectrodeSystemList implements Parcelable {

    public static final Creator<ElectrodeSystemList> CREATOR
            = new Creator<ElectrodeSystemList>() {
        public ElectrodeSystemList createFromParcel(Parcel in) {
            return new ElectrodeSystemList(in);
        }

        public ElectrodeSystemList[] newArray(int size) {
            return new ElectrodeSystemList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<ElectrodeSystem> electrodeSystems;

    public ElectrodeSystemList() {
    }

    public ElectrodeSystemList(Parcel in) {
        electrodeSystems = new ArrayList<ElectrodeSystem>();
        in.readTypedList(electrodeSystems, ElectrodeSystem.CREATOR);
    }

    public List<ElectrodeSystem> getElectrodeSystems() {
        return electrodeSystems;
    }

    public void setElectrodeSystems(List<ElectrodeSystem> electrodeSystems) {
        this.electrodeSystems = electrodeSystems;
    }

    public boolean isAvailable() {
        return electrodeSystems != null && !electrodeSystems.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(electrodeSystems);
    }
}
