package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Petr Miko
 *         Date: 9.3.13
 */
@Root(name = "subject")
public class SubjectData {

    @Element
    private int personId;
    @Element
    private String name, surname;
    @Element
    private String gender;
    @Element
    private int age;
    @Element
    private boolean leftHanded;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGender(char gender) {
        this.gender = String.valueOf(gender);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLeftHanded() {
        return leftHanded;
    }

    public void setLeftHanded(boolean leftHanded) {
        this.leftHanded = leftHanded;
    }
}
