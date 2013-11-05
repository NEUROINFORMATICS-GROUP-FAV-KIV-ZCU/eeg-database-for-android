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
 * Data container for digitization information.
 *
 * @author Petr Miko
 */
@Root(name = "digitization")
public class Digitization implements Parcelable {

    public static final Parcelable.Creator<Digitization> CREATOR
            = new Parcelable.Creator<Digitization>() {
        public Digitization createFromParcel(Parcel in) {
            return new Digitization(in);
        }

        public Digitization[] newArray(int size) {
            return new Digitization[size];
        }
    };
    @Element(required = false)
    private int digitizationId;
    @Element
    private String filter;
    @Element
    private float gain, samplingRate;

    public Digitization() {
    }

    public Digitization(Parcel in) {
        digitizationId = in.readInt();
        filter = in.readString();
        gain = in.readFloat();
        samplingRate = in.readFloat();
    }

    public int getDigitizationId() {
        return digitizationId;
    }

    public void setDigitizationId(int digitizationId) {
        this.digitizationId = digitizationId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public float getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(float samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(digitizationId);
        dest.writeString(filter);
        dest.writeFloat(gain);
        dest.writeFloat(samplingRate);
    }
}
