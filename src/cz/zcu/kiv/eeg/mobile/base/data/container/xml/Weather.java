package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

/**
 * @author Petr Miko
 *         Date: 9.3.13
 */

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "weather")
public class Weather {

    @Element
    private int weatherId;
    @Element
    private String title;
    @Element(required = false)
    private String description;

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
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
