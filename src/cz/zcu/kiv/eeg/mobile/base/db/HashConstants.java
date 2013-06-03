package cz.zcu.kiv.eeg.mobile.base.db;

/**
 * Created with IntelliJ IDEA.
 * User: Petr
 * Date: 1.6.13
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
public enum HashConstants {

    PEOPLES("People"),
    SCENARIOS("Scenarios"),
    DIGITISATION("Digitisation"),
    ARTIFACTS("Artifacts"),
    DISEASES("Diseases"),
    ELECTRODES_FIX("ElectrodeFix"),
    ELECTRODES_LOC("ElectrodeLocations"),
    EXPERIMENTS("Experiments"),
    RESERVATIONS("Reservations"),
    WEATHERS("Weathers"),
    ELECTRODE_SYSTEMS("ElectrodeSystems"),
    ELECTRODE_TYPES("ElectrodeTypes"),
    HARDWARE("Hardware") ,
    PHARMACEUTICALS("Pharmaceuticals"),
    RESEARCH_GROUPS("ResearchGroups"),
    SOFTWARE("Software")
  ;

    private String value;
    private HashConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
