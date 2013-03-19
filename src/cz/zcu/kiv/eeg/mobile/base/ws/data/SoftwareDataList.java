package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Container for holding used software information.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "softwareList")
public class SoftwareDataList {

    @ElementList(inline = true, required = false)
    private List<SoftwareData> softwareList;

    public List<SoftwareData> getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(List<SoftwareData> softwareList) {
        this.softwareList = softwareList;
    }
}
