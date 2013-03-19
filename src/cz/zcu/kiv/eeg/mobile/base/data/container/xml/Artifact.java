package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for holding artifact information.
 *
 * @author Petr Miko
 */
@Root(name = "artifact")
public class Artifact {

    @Element
    private int artifactId;
    @Element
    private String compensation;
    @Element
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
