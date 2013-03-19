package cz.zcu.kiv.eeg.mobile.base.data;

import cz.zcu.kiv.eeg.mobile.base.data.container.xml.UserInfo;

import java.util.Calendar;

/**
 * Gathered values used in application.
 * Values' names are descriptive enough.
 */
public class Values {

    //activitity results flags
    public final static int ADD_RECORD_FLAG = 1;
    public static final int SELECT_FILE_FLAG = 2;
    public final static String ADD_RECORD_KEY = "addRecord";
    public static final String FILE_PATH = "filePath";

    //shared preferences keys
    public final static String PREFS_CREDENTIALS = "AccountCredentials";
    public final static String PREFS_VARIOUS = "VariousValues";

    //REST service suffixes
    public final static String ENDPOINT = "/rest";
    public final static String SERVICE_DATAFILE = "/datafile/";
    public final static String SERVICE_EXPERIMENTS = "/experiments/";
    public final static String SERVICE_SCENARIOS = "/scenarios/";
    public final static String SERVICE_RESEARCH_GROUPS = "/groups/";
    public final static String SERVICE_USER = "/user/";
    public final static String SERVICE_RESERVATION = "/reservation/";

    //REST service qualifiers
    public final static String SERVICE_QUALIFIER_MINE = "mine";
    public final static String SERVICE_QUALIFIER_ALL = "all";

    //logged in user information
    public static UserInfo user;

    //day displayed as first in calendar view
    public static int firstDayOfWeek = Calendar.SUNDAY;

}
