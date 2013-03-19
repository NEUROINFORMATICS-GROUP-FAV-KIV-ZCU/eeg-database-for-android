package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for holding information about used pharmaceutics.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "pharmaceutical")
public class Pharmaceutical {

    @Element
    private int id;
    @Element
    private String title;
    @Element(required = false)
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}