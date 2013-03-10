package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for Experiment information.
 *
 * @author Petr Miko
 */
@Root(name = "experiment")
public class ExperimentData {

    @Element
    private int experimentId;
    @Element
    private ScenarioSimpleData scenario;
    @Element(required = false)
    private ArtifactData artifact;
    @Element
    private DigitizationData digitization;
    @Element
    private String startTime, endTime;
    @Element
    private DiseaseDataList diseases;
    @Element(required = false)
    private String environmentNote;
    @Element
    private WeatherData weather;
    @Element
    private SubjectData subject;

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEnvironmentNote() {
        return environmentNote;
    }

    public void setEnvironmentNote(String environmentNote) {
        this.environmentNote = environmentNote;
    }

    public ScenarioSimpleData getScenario() {
        return scenario;
    }

    public void setScenario(ScenarioSimpleData scenario) {
        this.scenario = scenario;
    }

    public ArtifactData getArtifact() {
        return artifact;
    }

    public void setArtifact(ArtifactData artifact) {
        this.artifact = artifact;
    }

    public DigitizationData getDigitization() {
        return digitization;
    }

    public DiseaseDataList getDiseases() {
        return diseases;
    }

    public void setDiseases(DiseaseDataList diseases) {
        this.diseases = diseases;
    }

    public void setDigitization(DigitizationData digitization) {
        this.digitization = digitization;
    }

    public WeatherData getWeather() {
        return weather;
    }

    public void setWeather(WeatherData weather) {
        this.weather = weather;
    }

    public SubjectData getSubject() {
        return subject;
    }

    public void setSubject(SubjectData subject) {
        this.subject = subject;
    }
}
