package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "experiments")
public class ExperimentDataList {

    @ElementList(inline = true, required = false)
    private List<ExperimentData> experiments;

    public List<ExperimentData> getExperiments() {
        return experiments;
    }

    public void setGroups(List<ExperimentData> experiments) {
        this.experiments = experiments;
    }

}