package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for Research Group information.
 * 
 * @author Petr Miko
 * 
 */
@Root(name = "researchGroup")
public class ResearchGroupData {

	@Element
	private int groupId;
	@Element
	private String groupName;

	public ResearchGroupData() {
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
