package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Weather data container.
 *
 * @author Petr Miko
 */
public class Weather implements Serializable {

    private int id;
    private String title;
    private String description;

    /**
     * Identifier getter.
     *
     * @return weather identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Identifier setter.
     *
     * @param id weather identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter of weather title.
     *
     * @return weather name/title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter of weather title.
     *
     * @param title weather name/title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter of weather description.
     *
     * @return weather description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of weather description.
     *
     * @param description weather description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
