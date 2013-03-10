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
    private Artifact artifact;
    private Digitization digitization;

    public Experiment(int experimentId, String startTime, String endTime, int scenarioId, String scenarioName) throws ParseException {
        this.experimentId = experimentId;
        this.startTime = sf.parse(startTime);
        this.endTime = sf.parse(endTime);
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
    }

    public Experiment(int experimentId, Date startTime, Date endTime, int scenarioId, String scenarioName) {
        this.experimentId = experimentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEnvironmentNote() {
        return environmentNote;
    }

    public void setEnvironmentNote(String environmentNote) {
        this.environmentNote = environmentNote;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public Person getSubject() {
        return subject;
    }

    public void setSubject(Person subject) {
        this.subject = subject;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public void setDigitization(Digitization digitization) {
        this.digitization = digitization;
    }

    public Digitization getDigitization() {
        return digitization;
    }
}
