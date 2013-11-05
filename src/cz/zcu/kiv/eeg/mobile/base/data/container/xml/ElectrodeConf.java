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
 * Data container for XML marshaling of electrode configuration data.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocation")
public class ElectrodeConf implements Parcelable{

    @Element
    private int id;
    @Element
    private int impedance;
    @Element
    private ElectrodeSystem electrodeSystem;
    @Element(required = false)
    private ElectrodeLocationList electrodeLocations;

    public ElectrodeConf(){}

    public ElectrodeConf(Parcel in){
        id = in.readInt();
        impedance = in.readInt();
        electrodeSystem = in.readParcelable(ElectrodeSystem.class.getClassLoader());
        electrodeLocations = in.readParcelable(ElectrodeLocationList.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImpedance() {
        return impedance;
    }

    public void setImpedance(int impedance) {
        this.impedance = impedance;
    }

    public ElectrodeSystem getElectrodeSystem() {
        return electrodeSystem;
    }

    public void setElectrodeSystem(ElectrodeSystem electrodeSystem) {
        this.electrodeSystem = electrodeSystem;
    }

    public ElectrodeLocationList getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(ElectrodeLocationList electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(impedance);
        dest.writeParcelable(electrodeSystem, flags);
        dest.writeParcelable(electrodeLocations, flags);
    }

    public static final Parcelable.Creator<ElectrodeConf> CREATOR
            = new Parcelable.Creator<ElectrodeConf>() {
        public ElectrodeConf createFromParcel(Parcel in) {
            return new ElectrodeConf(in);
        }

        public ElectrodeConf[] newArray(int size) {
            return new ElectrodeConf[size];
        }
    };
}
