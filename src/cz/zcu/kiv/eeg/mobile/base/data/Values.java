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
package cz.zcu.kiv.eeg.mobile.base.data;

import cz.zcu.kiv.eeg.mobile.base.data.container.xml.UserInfo;

import java.util.Calendar;

/**
 * Gathered values used in application.
 * Values' names are descriptive enough.
 */
public class Values {

    //activity results flags
    public static final int ADD_RESERVATION_FLAG = 1;
    public static final int SELECT_FILE_FLAG = 2;

    public static final String FILE_PATH = "filePath";

    //shared preferences keys
    public static final String PREFS_CREDENTIALS = "AccountCredentials";
    public static final String PREFS_VARIOUS = "VariousValues";

    //REST service suffixes
    public static final String ENDPOINT = "/rest";
    public static final String SERVICE_DATAFILE = "/datafile/";
    public static final String SERVICE_EXPERIMENTS = "/experiments/";
    public static final String SERVICE_SCENARIOS = "/scenarios/";
    public static final String SERVICE_RESEARCH_GROUPS = "/groups/";
    public static final String SERVICE_USER = "/user/";
    public static final String SERVICE_RESERVATION = "/reservation/";
    public static final String SERVICE_ARTIFACTS = "/experiments/artifacts";
    public static final String SERVICE_DIGITIZATIONS = "/experiments/digitizations";
    public static final String SERVICE_DISEASES = "/experiments/diseases";
    public static final String SERVICE_HARDWARE = "/experiments/hardwareList";
    public static final String SERVICE_SOFTWARE = "/experiments/softwareList";
    public static final String SERVICE_PHARMACEUTICAL = "/experiments/pharmaceuticals";
    public static final String SERVICE_ELECTRODE_LOCATIONS = "/experiments/electrodeLocations";
    public static final String SERVICE_ELECTRODE_FIXLIST = "/experiments/electrodeFixList";
    public static final String SERVICE_ELECTRODE_TYPES = "/experiments/electrodeTypes";
    public static final String SERVICE_ELECTRODE_SYSTEMS = "/experiments/electrodeSystems";
    public static final String SERVICE_WEATHER = "/experiments/weatherList";

    //REST service qualifiers
    public static final String SERVICE_QUALIFIER_MINE = "mine";
    public static final String SERVICE_QUALIFIER_ALL = "all";

    //logged in user information
    public static UserInfo user;

    //day displayed as first in calendar view
    public static int firstDayOfWeek = Calendar.SUNDAY;

    //bolean values in bytes for parcelable implementation
    public static byte TRUE = 1;
    public static byte FALSE = 0;

    public static final int ADD_SCENARIO_FLAG = 20;
    public static final int ADD_PERSON_FLAG = 21;
    public static final int ADD_ELECTRODE_LOCATION_FLAG = 22;
    public static final int ADD_DIGITIZATION_FLAG = 23;
    public static final int ADD_ARTIFACT_FLAG = 24;
    public static final int ADD_DISEASE_FLAG = 25;
    public static final int ADD_ELECTRODE_FIX_FLAG = 26;
    public static final int ADD_EXPERIMENT_FLAG = 27;
    public static final int ADD_WEATHER_FLAG = 28;


    public static final String ADD_RESERVATION_KEY = "addReservation";
    public static final String ADD_PERSON_KEY = "addPerson";
    public static final String ADD_SCENARIO_KEY = "addScenario";
    public static final String ADD_ELECTRODE_LOCATION_KEY = "addElectrodeLocation";
    public static final String ADD_ARTIFACT_KEY = "addArtifact";
    public static final String ADD_DIGITIZATION_KEY = "addDigitization";
    public static final String ADD_DISEASE_KEY = "addDisease";
    public static final String ADD_ELECTRODE_FIX_KEY = "addElectrodeFix";
    public static final String ADD_EXPERIMENT_KEY = "addExperiment";
    public static final String ADD_WEATHER_KEY = "addWeather";
}
