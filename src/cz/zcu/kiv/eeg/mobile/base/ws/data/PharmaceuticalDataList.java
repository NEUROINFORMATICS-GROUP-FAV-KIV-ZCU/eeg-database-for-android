package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Data container for holding collection of used pharmaceutics.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name="pharmaceuticals")
public class PharmaceuticalDataList {

    @ElementList(inline = true, required = false)
    private List<PharmaceuticalData> pharmaceuticals;

    public List<PharmaceuticalData> getPharmaceuticals() {
        return pharmaceuticals;
    }

    public void setPharmaceuticals(List<PharmaceuticalData> pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }
}
