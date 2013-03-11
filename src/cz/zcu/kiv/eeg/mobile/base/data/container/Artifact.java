package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Data container for artifact information.
 *
 * @author Petr Miko
 */
public class Artifact implements Serializable {

    private int artifactId;
    private String compensation;
    private String rejectCondition;

    /**
     * Identifier getter.
     *
     * @return artifact identifier
     */
    public int getArtifactId() {
        return artifactId;
    }

    /**
     * Setter of artifact identifier.
     *
     * @param artifactId artifact identifier
     */
    public void setArtifactId(int artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * Compensation getter.
     *
     * @return compensation
     */
    public String getCompensation() {
        return compensation;
    }

    /**
     * Compensation setter.
     *
     * @param compensation compensation
     */
    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    /**
     * Reject condition getter.
     *
     * @return reject condition value
     */
    public String getRejectCondition() {
        return rejectCondition;
    }

    /**
     * Reject condition setter.
     *
     * @param rejectCondition reject condition value
     */
    public void setRejectCondition(String rejectCondition) {
        this.rejectCondition = rejectCondition;
    }
}
