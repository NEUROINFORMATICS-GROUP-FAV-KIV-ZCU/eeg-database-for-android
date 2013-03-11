package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * @author Petr Miko
 *         Date: 10.3.13
 */
public class Artifact implements Serializable {

    private int artifactId;
    private String compensation;
    private String rejectCondition;

    public int getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(int artifactId) {
        this.artifactId = artifactId;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    public String getRejectCondition() {
        return rejectCondition;
    }

    public void setRejectCondition(String rejectCondition) {
        this.rejectCondition = rejectCondition;
    }
}
