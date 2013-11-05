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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Container for holding of available record count.
 *
 * @author Petr Miko
 */
@Root(name = "recordCount")
public class RecordCount {

    @Element
    private int myRecords;
    @Element
    private int publicRecords;

    public int getMyRecords() {
        return myRecords;
    }

    public void setMyRecords(int myRecords) {
        this.myRecords = myRecords;
    }

    public int getPublicRecords() {
        return publicRecords;
    }

    public void setPublicRecords(int publicRecords) {
        this.publicRecords = publicRecords;
    }
}
