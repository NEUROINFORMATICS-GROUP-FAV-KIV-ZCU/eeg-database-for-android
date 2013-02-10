package cz.zcu.kiv.eeg.mobile.base.data;

import cz.zcu.kiv.eeg.mobile.base.ws.data.UserInfo;

public class Constants {
	public final static int ADD_RECORD_FLAG = 1;
    public static final int SELECT_FILE_FLAG = 2;
	public final static String ADD_RECORD_KEY = "addRecord";
    public static final String FILE_PATH = "filePath";
	public final static String PREFS_CREDENTIALS = "AccountCredentials";
	public final static String ENDPOINT = "/rest";
	public final static String SERVICE_USER = "/user/";
	public final static String SERVICE_RESERVATION = "/reservation/";
	public static UserInfo user;
}
