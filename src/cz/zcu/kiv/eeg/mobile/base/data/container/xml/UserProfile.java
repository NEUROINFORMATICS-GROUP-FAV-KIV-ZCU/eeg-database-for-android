package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

/**
 * Created by cyclops on 7/2/15.
 */
public class UserProfile {

    private String userName;
    private String userId;

    public UserProfile(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
