package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for XML marshaling of electrode configuration data.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocation")
public class ElectrodeConfData {

    @Element
    private int id;
    @Element
    private int impedance;
    @Element
    private ElectrodeSystemData electrodeSystem;
    @Element
    private ElectrodeLocationDataList electrodeLocations;

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

    public ElectrodeSystemData getElectrodeSystem() {
        return electrodeSystem;
    }

    public void setElectrodeSystem(ElectrodeSystemData electrodeSystem) {
        this.electrodeSystem = electrodeSystem;
    }

    public ElectrodeLocationDataList getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(ElectrodeLocationDataList electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
