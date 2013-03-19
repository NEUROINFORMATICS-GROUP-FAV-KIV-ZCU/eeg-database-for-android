package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for Experiment information.
 *
 * @author Petr Miko
 */
@Root(name = "experiment", strict = false)
public class Experiment implements Parcelable {

    public static final Parcelable.Creator<Experiment> CREATOR
            = new Parcelable.Creator<Experiment>() {
        public Experiment createFromParcel(Parcel in) {
            return new Experiment(in);
        }

        public Experiment[] newArray(int size) {
            return new Experiment[size];
        }
    };
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

    public Experiment() {
    }

    public Experiment(Parcel in) {
        experimentId = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
        environmentNote = in.readString();
        temperature = in.readInt();
        scenario = in.readParcelable(Scenario.class.getClassLoader());
        researchGroup = in.readParcelable(ResearchGroup.class.getClassLoader());
        artifact = in.readParcelable(Artifact.class.getClassLoader());
        digitization = in.readParcelable(Digitization.class.getClassLoader());
        diseases = in.readParcelable(DiseaseList.class.getClassLoader());
        hardwareList = in.readParcelable(HardwareList.class.getClassLoader());
        weather = in.readParcelable(Weather.class.getClassLoader());
        owner = in.readParcelable(Owner.class.getClassLoader());
        subject = in.readParcelable(Subject.class.getClassLoader());
        softwareList = in.readParcelable(SoftwareList.class.getClassLoader());
        pharmaceuticals = in.readParcelable(PharmaceuticalList.class.getClassLoader());
        electrodeConf = in.readParcelable(ElectrodeConf.class.getClassLoader());
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(experimentId);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(environmentNote);
        dest.writeInt(temperature);
        dest.writeParcelable(scenario, flags);
        dest.writeParcelable(researchGroup, flags);
        dest.writeParcelable(artifact, flags);
        dest.writeParcelable(digitization, flags);
        dest.writeParcelable(diseases, flags);
        dest.writeParcelable(hardwareList, flags);
        dest.writeParcelable(weather, flags);
        dest.writeParcelable(owner, flags);
        dest.writeParcelable(subject, flags);
        dest.writeParcelable(softwareList, flags);
        dest.writeParcelable(pharmaceuticals, flags);
        dest.writeParcelable(electrodeConf, flags);
    }
}
