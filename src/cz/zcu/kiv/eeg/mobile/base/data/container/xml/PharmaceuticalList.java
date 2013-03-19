package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Data container for holding collection of used pharmaceutics.
 * Used for XML marshaling.
 *
 * @author Petr Miko
 */
@Root(name = "pharmaceuticals")
public class PharmaceuticalList {

    @ElementList(inline = true, required = false)
    private List<Pharmaceutical> pharmaceuticals;

    public List<Pharmaceutical> getPharmaceuticals() {
        return pharmaceuticals;
    }

    public void setPharmaceuticals(List<Pharmaceutical> pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }
}
