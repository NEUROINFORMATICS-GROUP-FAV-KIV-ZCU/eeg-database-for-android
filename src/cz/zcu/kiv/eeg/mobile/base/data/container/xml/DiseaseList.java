package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
@Root(name = "diseases")
public class DiseaseList {

    @ElementList(inline = true, required = false)
    private List<Disease> diseases;

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }
}
