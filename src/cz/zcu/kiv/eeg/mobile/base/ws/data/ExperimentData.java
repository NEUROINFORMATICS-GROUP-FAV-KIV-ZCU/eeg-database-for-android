package cz.zcu.kiv.eeg.mobile.base.ws.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for Experiment information.
 *
 * @author Petr Miko
 */
@Root(name = "experiment", strict = false)
public class ExperimentData {

    @Element
    private int experimentId;
    @Element
    private ScenarioSimpleData scenario;
    @Element
    private ResearchGroupData researchGroup;
    @Element(required = false)
    private ArtifactData artifact;
    @Element
    private DigitizationData digitization;
    @Element
    private String startTime, endTime;
    @Element
    private DiseaseDataList diseases;
    @Element
    private HardwareDataList hardwareList;
    @Element(required = false)
    private String environmentNote;
    @Element
    private WeatherData weather;
    @Element
    private OwnerData owner;
    @Element
    private SubjectData subject;
    @Element(required = false)
    private Integer temperature;
    @Element
    private SoftwareDataList softwareList;
    @Element
    private PharmaceuticalDataList pharmaceuticals;
    @Element
    private ElectrodeConfData electrodeConf;

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

    public ResearchGroupData getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(ResearchGroupData researchGroup) {
        this.researchGroup = researchGroup;
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

    public void setDigitization(DigitizationData digitization) {
        this.digitization = digitization;
    }

    public DiseaseDataList getDiseases() {
        return diseases;
    }

    public void setDiseases(DiseaseDataList diseases) {
        this.diseases = diseases;
    }

    public WeatherData getWeather() {
        return weather;
    }

    public void setWeather(WeatherData weather) {
        this.weather = weather;
    }

    public OwnerData getOwner() {
        return owner;
    }

    public void setOwner(OwnerData owner) {
        this.owner = owner;
    }

    public SubjectData getSubject() {
        return subject;
    }

    public void setSubject(SubjectData subject) {
        this.subject = subject;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public HardwareDataList getHardwareList() {
        return hardwareList;
    }

    public void setHardwareList(HardwareDataList hardwareList) {
        this.hardwareList = hardwareList;
    }

    public SoftwareDataList getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(SoftwareDataList softwareList) {
        this.softwareList = softwareList;
    }

    public PharmaceuticalDataList getPharmaceuticals() {
        return pharmaceuticals;
    }

    public void setPharmaceuticals(PharmaceuticalDataList pharmaceutics) {
        this.pharmaceuticals = pharmaceutics;
    }

    public ElectrodeConfData getElectrodeConf() {
        return electrodeConf;
    }

    public void setElectrodeConf(ElectrodeConfData electrodeConf) {
        this.electrodeConf = electrodeConf;
    }
}
