package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container of electrode location data for XML marshaling purposes.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocation")
public class ElectrodeLocation {

    @Element
    private int id;
    @Element
    private String title;
    @Element(required = false)
    private String abbr;
    @Element(required = false)
    private String description;
    @Element(required = false)
    private int defaultNumber;
    @Element
    private ElectrodeFix electrodeFix;
    @Element
    private ElectrodeType electrodeType;

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

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
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

    public ElectrodeFix getElectrodeFix() {
        return electrodeFix;
    }

    public void setElectrodeFix(ElectrodeFix electrodeFix) {
        this.electrodeFix = electrodeFix;
    }

    public ElectrodeType getElectrodeType() {
        return electrodeType;
    }

    public void setElectrodeType(ElectrodeType electrodeType) {
        this.electrodeType = electrodeType;
    }
}
