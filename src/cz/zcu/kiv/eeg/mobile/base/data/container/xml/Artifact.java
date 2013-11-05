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
 * Data container for holding artifact information.
 *
 * @author Petr Miko
 */
@Root(name = "artifact")
public class Artifact implements Parcelable {

    public static final Parcelable.Creator<Artifact> CREATOR
            = new Parcelable.Creator<Artifact>() {
        public Artifact createFromParcel(Parcel in) {
            return new Artifact(in);
        }

        public Artifact[] newArray(int size) {
            return new Artifact[size];
        }
    };

    @Element
    private int artifactId;
    @Element
    private String compensation;
    @Element
    private String rejectCondition;

    public Artifact(){}

    public Artifact(Parcel in) {
        artifactId = in.readInt();
        compensation = in.readString();
        rejectCondition = in.readString();
    }

    public int getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(int artifactId) {
        this.artifactId = artifactId;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    public String getRejectCondition() {
        return rejectCondition;
    }

    public void setRejectCondition(String rejectCondition) {
        this.rejectCondition = rejectCondition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(artifactId);
        dest.writeString(compensation);
        dest.writeString(rejectCondition);
    }
}
