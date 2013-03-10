package cz.zcu.kiv.eeg.mobile.base.ws.data;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "researchGroups")
public class ResearchGroupDataList {

	@ElementList(inline = true, required = false)
	private List<ResearchGroupData> groups;

	public List<ResearchGroupData> getGroups() {
		return groups;
	}

	public void setGroups(List<ResearchGroupData> groups) {
		this.groups = groups;
	}
}
