package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
public class Disease implements Serializable {

    private int id;
    private String name, description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
