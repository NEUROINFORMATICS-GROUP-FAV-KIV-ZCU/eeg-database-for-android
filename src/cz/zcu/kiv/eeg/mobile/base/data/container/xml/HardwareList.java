package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
@Root(name = "hardwareList")
public class HardwareList {

    @ElementList(inline = true, required = false)
    private List<Hardware> hardwareList;

    public List<Hardware> getHardwareList() {
        return hardwareList;
    }

    public void setHardwareList(List<Hardware> hardwareList) {
        this.hardwareList = hardwareList;
    }
}
