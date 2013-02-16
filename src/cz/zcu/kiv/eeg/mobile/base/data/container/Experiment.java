package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Experiment data container.
 *
 * @author Petr Miko
 */
public class Experiment {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private int experimentId;
    private Date startTime, endTime;
    private String environmentNote;
    private int scenarioId;
    private String scenarioName;

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
}
