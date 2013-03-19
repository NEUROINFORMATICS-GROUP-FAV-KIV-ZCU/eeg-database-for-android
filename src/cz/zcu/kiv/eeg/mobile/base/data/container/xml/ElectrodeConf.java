package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for XML marshaling of electrode configuration data.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocation")
public class ElectrodeConf {

    @Element
    private int id;
    @Element
    private int impedance;
    @Element
    private ElectrodeSystem electrodeSystem;
    @Element(required = false)
    private ElectrodeLocationList electrodeLocations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImpedance() {
        return impedance;
    }

    public void setImpedance(int impedance) {
        this.impedance = impedance;
    }

    public ElectrodeSystem getElectrodeSystem() {
        return electrodeSystem;
    }

    public void setElectrodeSystem(ElectrodeSystem electrodeSystem) {
        this.electrodeSystem = electrodeSystem;
    }

    public ElectrodeLocationList getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(ElectrodeLocationList electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
