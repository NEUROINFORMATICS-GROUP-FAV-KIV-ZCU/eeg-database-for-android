package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
@Root(name = "hardwareList")
public class HardwareDataList {

    @ElementList(inline = true, required = false)
    private List<HardwareData> hardwareList;

    public List<HardwareData> getHardwareList() {
        return hardwareList;
    }

    public void setHardwareList(List<HardwareData> hardwareList) {
        this.hardwareList = hardwareList;
    }
}
