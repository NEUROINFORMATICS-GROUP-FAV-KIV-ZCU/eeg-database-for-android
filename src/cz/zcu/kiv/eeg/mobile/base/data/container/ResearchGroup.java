package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Data container for Research Group information.
 *
 * @author Petr Miko
 */
public class ResearchGroup implements Serializable {

    private static final long serialVersionUID = -121212115839199552L;
    /**
     * Research group identifier.
     */
    private int researchGroupId;
    /**
     * Research group name.
     */
    private String researchGroupName;

    /**
     * Creates uninitialized research group object.
     */
    public ResearchGroup() {
    }

    /**
     * Creates initialized research group object.
     *
     * @param researchGroupId   research group identifier
     * @param researchGroupName research group name
     */
    public ResearchGroup(int researchGroupId, String researchGroupName) {
        this.researchGroupId = researchGroupId;
        this.researchGroupName = researchGroupName;
    }

    /**
     * Gets research group identifier.
     *
     * @return research group identifier
     */
    public int getResearchGroupId() {
        return researchGroupId;
    }

    /**
     * Sets research group identifier.
     *
     * @param researchGroupId research group identifier
     */
    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    /**
     * Gets research group name.
     *
     * @return research group name
     */
    public String getResearchGroupName() {
        return researchGroupName;
    }

    /**
     * Sets research group name.
     *
     * @param researchGroupName research group name
     */
    public void setResearchGroupName(String researchGroupName) {
        this.researchGroupName = researchGroupName;
    }
}
