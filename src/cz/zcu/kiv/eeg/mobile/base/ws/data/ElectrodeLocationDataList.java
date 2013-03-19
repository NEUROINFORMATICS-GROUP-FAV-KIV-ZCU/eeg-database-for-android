package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Data container for collection of electrode location data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "electrodeLocations")
public class ElectrodeLocationDataList {

    @ElementList(inline = true, required = false)
    private List<ElectrodeLocationData> electrodeLocations;

    public List<ElectrodeLocationData> getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(List<ElectrodeLocationData> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
