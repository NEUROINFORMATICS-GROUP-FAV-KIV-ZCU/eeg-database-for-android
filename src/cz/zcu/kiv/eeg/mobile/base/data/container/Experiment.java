package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Experiment data container.
 *
 * @author Petr Miko
 */
public class Experiment implements Serializable {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private int experimentId;
    private Date startTime, endTime;
    private String environmentNote;
    private int scenarioId;
    private String scenarioName;
    private Person subject;
    private Weather weather;
    private List<Disease> diseases;
    private List<Hardware> hardwares;
    private Artifact artifact;
    private Digitization digitization;
    private Integer temperature;

    /**
     * Experiment constructor.
     *
     * @param experimentId identifier
     * @param startTime    time when experiment started
     * @param endTime      time when experiment stopped
     * @param scenarioId   scenario identifier
     * @param scenarioName scenario name
     * @throws ParseException error occurred when tried to parse time strings
     */
    public Experiment(int experimentId, String startTime, String endTime, int scenarioId, String scenarioName) throws ParseException {
        this.experimentId = experimentId;
        this.startTime = sf.parse(startTime);
        this.endTime = sf.parse(endTime);
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
    }

    /**
     * Experiment constructor.
     *
     * @param experimentId identifier
     * @param startTime    time when experiment started
     * @param endTime      time when experiment stopped
     * @param scenarioId   scenario identifier
     * @param scenarioName scenario name
     */
    public Experiment(int experimentId, Date startTime, Date endTime, int scenarioId, String scenarioName) {
        this.experimentId = experimentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
    }

    /**
     * Identifier getter.
     *
     * @return experiment identifier
     */
    public int getExperimentId() {
        return experimentId;
    }

    /**
     * Identifier setter.
     *
     * @param experimentId experiment identifier
     */
    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * Getter of experiment start time.
     *
     * @return time when experiment started
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Setter of experiment start time.
     *
     * @param startTime time when experiment started
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter of experiment end time.
     *
     * @return time when experiment stopped
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Setter of experiment end time.
     *
     * @param endTime time when experiment stopped
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Getter of an environment note.
     *
     * @return environment note
     */
    public String getEnvironmentNote() {
        return environmentNote;
    }

    /**
     * Setter of an environment note.
     *
     * @param environmentNote environment note.
     */
    public void setEnvironmentNote(String environmentNote) {
        this.environmentNote = environmentNote;
    }

    /**
     * Getter of related scenario identifier.
     *
     * @return scenario identifier
     */
    public int getScenarioId() {
        return scenarioId;
    }

    /**
     * Setter of related scenario identifier.
     *
     * @param scenarioId scenario identifier
     */
    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * Getter of related scenario name.
     *
     * @return scenario name
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * Setter of related scenario name.
     *
     * @param scenarioName scenario name
     */
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    /**
     * Getter of measured subject.
     *
     * @return subject person
     */
    public Person getSubject() {
        return subject;
    }

    /**
     * Setter of measured subject.
     *
     * @param subject subject person
     */
    public void setSubject(Person subject) {
        this.subject = subject;
    }

    /**
     * Getter of weather during experiment measurement.
     *
     * @return weather information
     */
    public Weather getWeather() {
        return weather;
    }

    /**
     * Setter of weather during experiment measurement.
     *
     * @param weather weather information
     */
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    /**
     * Getter of subject's diseases.
     *
     * @return subject's diseases
     */
    public List<Disease> getDiseases() {
        return diseases;
    }

    /**
     * Setter of subject's diseases.
     *
     * @param diseases subject's diseases
     */
    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    /**
     * Getter of artifact occurred during measurement.
     *
     * @return artifact information
     */
    public Artifact getArtifact() {
        return artifact;
    }

    /**
     * Setter of artifact occurred during measurement.
     *
     * @param artifact artifact information
     */
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public Digitization getDigitization() {
        return digitization;
    }

    public void setDigitization(Digitization digitization) {
        this.digitization = digitization;
    }

    /**
     * Temperature during measurement.
     *
     * @return temperature value
     */
    public Integer getTemperature() {
        return temperature;
    }

    /**
     * Temperature during measurement.
     *
     * @param temperature temperature value
     */
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    /**
     * Getter of all hardware used during measurement.
     *
     * @return hardware collection
     */
    public List<Hardware> getHardwares() {
        return hardwares;
    }

    /**
     * Setter of all hardware used during measurement.
     *
     * @param hardwares hardware collection
     */
    public void setHardwares(List<Hardware> hardwares) {
        this.hardwares = hardwares;
    }
}
