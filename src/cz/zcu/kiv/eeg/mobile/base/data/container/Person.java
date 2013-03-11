package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Person data container.
 *
 * @author Petr Miko
 */
public class Person implements Serializable {

    private int id;
    private String name, surname, gender;
    private boolean leftHanded;
    private int age;

    /**
     * Identifier getter.
     *
     * @return identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Identifier setter.
     *
     * @param id identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Person name getter.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Person name setter.
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Person surname getter.
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Person surname setter.
     *
     * @param surname surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Person gender getter.
     *
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Person gender setter.
     *
     * @param gender gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Getter method, whether person is left-handed.
     *
     * @return is person left-handed
     */
    public boolean isLeftHanded() {
        return leftHanded;
    }

    /**
     * Getter method, whether person is left-handed.
     *
     * @param leftHanded is person left-handed
     */
    public void setLeftHanded(boolean leftHanded) {
        this.leftHanded = leftHanded;
    }

    /**
     * Age getter.
     *
     * @return person age
     */
    public int getAge() {
        return age;
    }

    /**
     * Age setter.
     *
     * @param age person age
     */
    public void setAge(int age) {
        this.age = age;
    }
}
