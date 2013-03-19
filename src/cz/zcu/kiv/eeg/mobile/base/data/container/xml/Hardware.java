package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
@Root(name = "hardware")
public class Hardware {

    @Element
    private int hardwareId;
    @Element
    private String title;
    @Element
    private String type;
    @Element(required = false)
    private String description;
    @Element
    private int defaultNumber;

    public int getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(int hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }
}
