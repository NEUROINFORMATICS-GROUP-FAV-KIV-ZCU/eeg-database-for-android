package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
@Root(name = "diseases")
public class DiseaseDataList {

    @ElementList(inline = true, required = false)
    private List<DiseaseData> diseases;

    public List<DiseaseData> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<DiseaseData> diseases) {
        this.diseases = diseases;
    }
}
