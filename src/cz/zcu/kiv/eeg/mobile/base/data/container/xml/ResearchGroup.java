package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for Research Group information.
 *
 * @author Petr Miko
 */
@Root(name = "researchGroup")
public class ResearchGroup {

    @Element
    private int groupId;
    @Element
    private String groupName;

    public ResearchGroup() {
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
