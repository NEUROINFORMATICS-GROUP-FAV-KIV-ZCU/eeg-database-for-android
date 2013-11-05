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
 * Data container for holding scenario collection.
 * Supports XML marshaling and is parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "scenarios")
public class ScenarioList implements Parcelable {

    public static final Parcelable.Creator<ScenarioList> CREATOR
            = new Parcelable.Creator<ScenarioList>() {
        public ScenarioList createFromParcel(Parcel in) {
            return new ScenarioList(in);
        }

        public ScenarioList[] newArray(int size) {
            return new ScenarioList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Scenario> scenarios;

    public ScenarioList() {
    }

    public ScenarioList(Parcel in) {
        scenarios = new ArrayList<Scenario>();
        in.readTypedList(scenarios, Scenario.CREATOR);
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public boolean isAvailable() {
        return scenarios != null && !scenarios.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(scenarios);
    }
}
