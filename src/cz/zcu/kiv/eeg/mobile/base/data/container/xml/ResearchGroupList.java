package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "researchGroups")
public class ResearchGroupList {

    @ElementList(inline = true, required = false)
    private List<ResearchGroup> groups;

    public List<ResearchGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ResearchGroup> groups) {
        this.groups = groups;
    }
}
