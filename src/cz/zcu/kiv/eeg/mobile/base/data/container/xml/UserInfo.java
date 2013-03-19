package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for user information.
 */

@Root(name = "user")
public class UserInfo {
    @Element
    private String name;
    @Element
    private String surname;
    @Element
    private String rights;

    public UserInfo() {
    }

    public UserInfo(String name, String surname, String rights) {
        this.name = name;
        this.surname = surname;
        this.rights = rights;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }
}