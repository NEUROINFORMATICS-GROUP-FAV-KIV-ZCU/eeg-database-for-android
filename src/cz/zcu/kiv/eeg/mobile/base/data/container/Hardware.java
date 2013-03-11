package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Hardware information container.
 *
 * @author Petr Miko
 */
public class Hardware implements Serializable {

    private int id;
    private String title, type, description;
    private int defaultNumber;

    /**
     * HW identifier.
     *
     * @return identifier
     */
    public int getId() {
        return id;
    }

    /**
     * HW identifier.
     *
     * @param id identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Hardware name/title getter.
     *
     * @return name/title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Hardware name/title setter.
     *
     * @param title name/title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter of HW type.
     *
     * @return hardware type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter of HW type.
     *
     * @param type hardware type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter of HW description.
     *
     * @return hardware description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of HW description.
     *
     * @param description hardware description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter of HW default number.
     *
     * @return default number
     */
    public int getDefaultNumber() {
        return defaultNumber;
    }

    /**
     * Setter of HW default number.
     *
     * @param defaultNumber default number
     */
    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }
}
