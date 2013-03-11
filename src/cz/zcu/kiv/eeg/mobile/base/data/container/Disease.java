package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Disease data container.
 *
 * @author Petr Miko
 */
public class Disease implements Serializable {

    private int id;
    private String name, description;

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
     * Disease name getter.
     *
     * @return disease name
     */
    public String getName() {
        return name;
    }

    /**
     * Disease name setter.
     *
     * @param name disease name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Disease description getter.
     *
     * @return disease description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Disease description setter.
     *
     * @param description disease description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
