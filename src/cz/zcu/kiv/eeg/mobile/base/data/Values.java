package cz.zcu.kiv.eeg.mobile.base.data;

import cz.zcu.kiv.eeg.mobile.base.ws.data.UserInfo;

import java.util.Calendar;

public class Values {
	public final static int ADD_RECORD_FLAG = 1;
    public static final int SELECT_FILE_FLAG = 2;
	public final static String ADD_RECORD_KEY = "addRecord";
    public static final String FILE_PATH = "filePath";
	public final static String PREFS_CREDENTIALS = "AccountCredentials";
    public final static String PREFS_VARIOUS = "VariousValues";
	public final static String ENDPOINT = "/rest";
    public final static String SERVICE_DATAFILE = "/datafile/";
    public final static String SERVICE_EXPERIMENTS = "/experiments/";
    public final static String SERVICE_SCENARIOS = "/scenarios/";
    public final static String SERVICE_RESEARCH_GROUPS = "/groups/";
	public final static String SERVICE_USER = "/user/";
	public final static String SERVICE_RESERVATION = "/reservation/";
    public final static String SERVICE_QUALIFIER_MINE = "mine";
    public final static String SERVICE_QUALIFIER_ALL = "all";
	public static UserInfo user;

    public static int firstDayOfWeek = Calendar.SUNDAY;

}
