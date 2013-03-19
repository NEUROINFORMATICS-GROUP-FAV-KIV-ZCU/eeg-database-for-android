package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container of basic information about scenario.
 * Support XML marshaling and is parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "scenario")
public class ScenarioSimple implements Parcelable {

    public static final Parcelable.Creator<ScenarioSimple> CREATOR
            = new Parcelable.Creator<ScenarioSimple>() {
        public ScenarioSimple createFromParcel(Parcel in) {
            return new ScenarioSimple(in);
        }

        public ScenarioSimple[] newArray(int size) {
            return new ScenarioSimple[size];
        }
    };
    @Element
    private int scenarioId;
    @Element
    private String scenarioName;

    public ScenarioSimple() {
    }

    public ScenarioSimple(Parcel in) {
        scenarioId = in.readInt();
        scenarioName = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(scenarioId);
        dest.writeString(scenarioName);
    }
}
