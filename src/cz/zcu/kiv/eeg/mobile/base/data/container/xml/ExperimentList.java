package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "experiments")
public class ExperimentList {

    @ElementList(inline = true, required = false)
    private List<Experiment> experiments;

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public void setGroups(List<Experiment> experiments) {
        this.experiments = experiments;
    }

}