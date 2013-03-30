package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.ParseException;

/**
 * Data container for Experiment information.
 *
 * @author Petr Miko
 */
@Root(name = "experiment", strict = false)
public class Experiment implements Parcelable {

    private final static String TAG = Experiment.class.getSimpleName();

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
    private TimeContainer startTime, endTime;
    @Element
    private DiseaseList diseases;
    @Element
    private HardwareList hardwareList;
    @Element(required = false)
    private String environmentNote;
    @Element
    private Weather weather;
    @Element(required = false)
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
        startTime = in.readParcelable(TimeContainer.class.getClassLoader());
        endTime = in.readParcelable(TimeContainer.class.getClassLoader());
        environmentNote = in.readString();
        temperature = in.readInt();
        scenario = in.readParcelable(ScenarioSimple.class.getClassLoader());
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

    public TimeContainer getStartTime() {
        return startTime;
    }

    public void setStartTime(TimeContainer startTime) {
        this.startTime = startTime;
    }

    public TimeContainer getEndTime() {
        return endTime;
    }

    public void setEndTime(TimeContainer endTime) {
        this.endTime = endTime;
    }

    @Element(name = "startTime")
    public void setStartTimeString(String startTime) {
        try {
            this.startTime = new TimeContainer(startTime, "dd.MM.yyyy HH:mm");
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            this.startTime = null;
        }
    }

    @Element(name = "endTime")
    public String getEndTimeString() {
        return endTime.toString();
    }

    @Element(name = "endTime")
    public void setEndTimeString(String endtime) {
        try {
            this.endTime = new TimeContainer(endtime, "dd.MM.yyyy HH:mm");
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            this.endTime = null;
        }
    }

    @Element(name = "startTime")
    public String getStartTimeString() {
        return startTime.toString();
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
        dest.writeParcelable(startTime, flags);
        dest.writeParcelable(endTime, flags);
        dest.writeString(environmentNote);
        dest.writeInt(temperature != null ? temperature : 0);
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
