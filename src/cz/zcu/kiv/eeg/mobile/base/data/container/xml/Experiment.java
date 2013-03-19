package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Data container for Experiment information.
 *
 * @author Petr Miko
 */
@Root(name = "experiment", strict = false)
public class Experiment implements Serializable {

    @Element
    private int experimentId;
    @Element
    private ScenarioSimple scenario;
    @Element
    private ResearchGroup researchGroup;
    @Element(required = false)
    private Artifact artifact;
    @Element
    private Digitization digitization;
    @Element
    private String startTime, endTime;
    @Element
    private DiseaseList diseases;
    @Element
    private HardwareList hardwareList;
    @Element(required = false)
    private String environmentNote;
    @Element
    private Weather weather;
    @Element
    private Owner owner;
    @Element
    private Subject subject;
    @Element(required = false)
    private Integer temperature;
    @Element
    private SoftwareList softwareList;
    @Element
    private PharmaceuticalList pharmaceuticals;
    @Element
    private ElectrodeConf electrodeConf;

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

    public ScenarioSimple getScenario() {
        return scenario;
    }

    public void setScenario(ScenarioSimple scenario) {
        this.scenario = scenario;
    }

    public ResearchGroup getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public Digitization getDigitization() {
        return digitization;
    }

    public void setDigitization(Digitization digitization) {
        this.digitization = digitization;
    }

    public DiseaseList getDiseases() {
        return diseases;
    }

    public void setDiseases(DiseaseList diseases) {
        this.diseases = diseases;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public HardwareList getHardwareList() {
        return hardwareList;
    }

    public void setHardwareList(HardwareList hardwareList) {
        this.hardwareList = hardwareList;
    }

    public SoftwareList getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(SoftwareList softwareList) {
        this.softwareList = softwareList;
    }

    public PharmaceuticalList getPharmaceuticals() {
        return pharmaceuticals;
    }

    public void setPharmaceuticals(PharmaceuticalList pharmaceutics) {
        this.pharmaceuticals = pharmaceutics;
    }

    public ElectrodeConf getElectrodeConf() {
        return electrodeConf;
    }

    public void setElectrodeConf(ElectrodeConf electrodeConf) {
        this.electrodeConf = electrodeConf;
    }
}
