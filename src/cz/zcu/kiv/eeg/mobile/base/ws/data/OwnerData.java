package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Petr Miko
 *         Date: 19.3.13
 */
@Root(name = "owner")
public class OwnerData {

    @Element
    private int id;
    @Element
    private String name;
    @Element
    private String surname;
    @Element(required = false)
    private String mailUsername, mailDomain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailDomain() {
        return mailDomain;
    }

    public void setMailDomain(String mailDomain) {
        this.mailDomain = mailDomain;
    }
}
