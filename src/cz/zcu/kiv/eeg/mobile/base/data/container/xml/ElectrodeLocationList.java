package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

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
public class ElectrodeLocationList {

    @ElementList(inline = true, required = false)
    private List<ElectrodeLocation> electrodeLocations;

    public List<ElectrodeLocation> getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(List<ElectrodeLocation> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
