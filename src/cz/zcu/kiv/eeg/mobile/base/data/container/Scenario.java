package cz.zcu.kiv.eeg.mobile.base.data.container;

import java.io.Serializable;

/**
 * Data container for Scenario information.
 *
 * @author Petr Miko
 */
public class Scenario implements Serializable {

    private int scenarioId;
    private String scenarioName;
    private String ownerName;
    private String researchGroupName;
    private String description;
    private boolean isPrivate;
    private String mimeType;
    private String fileName;
    private String fileLength;

    /**
     * Scenario identifier getter.
     *
     * @return scenario identifier
     */
    public int getScenarioId() {
        return scenarioId;
    }

    /**
     * Scenario identifier setter.
     *
     * @param scenarioId scenario identifier
     */
    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * Getter of scenario owner name.
     *
     * @return owner name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Setter of scenario owner name.
     *
     * @param ownerName owner name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Getter of research group name.
     *
     * @return group name
     */
    public String getResearchGroupName() {
        return researchGroupName;
    }

    /**
     * Setter of research group name.
     *
     * @param researchGroupName group name
     */
    public void setResearchGroupName(String researchGroupName) {
        this.researchGroupName = researchGroupName;
    }

    /**
     * Getter of scenario name.
     *
     * @return scenario name
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * Setter of scenario name.
     *
     * @param scenarioName scenario name
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * Getter of scenario description.
     *
     * @return scenario description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of scenario description.
     *
     * @param description scenario description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checker method, whether is scenario private.
     *
     * @return is scenario private
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Setter of scenario private state.
     *
     * @param aPrivate is scenario private
     */
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    /**
     * Scenario file MIME type getter.
     *
     * @return scenario file MIME type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Scenario file MIME type setter.
     *
     * @param mimeType scenario file MIME type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Getter of scenario file name.
     *
     * @return file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Setter of scenario file name.
     *
     * @param fileName scenario file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Getter of scenario file size in bytes.
     *
     * @return file size in bytes
     */
    public String getFileLength() {
        return fileLength;
    }

    /**
     * Setter of scenario file size in bytes.
     *
     * @param fileLength file size in bytes
     */
    public void setFileLength(String fileLength) {
        this.fileLength = fileLength;
    }
}
