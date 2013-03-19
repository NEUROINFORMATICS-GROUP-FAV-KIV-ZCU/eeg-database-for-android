package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

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
public class SoftwareList {

    @ElementList(inline = true, required = false)
    private List<Software> softwareList;

    public List<Software> getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(List<Software> softwareList) {
        this.softwareList = softwareList;
    }
}
