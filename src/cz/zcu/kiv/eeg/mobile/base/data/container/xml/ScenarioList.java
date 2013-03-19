package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding scenario collection.
 * Supports XML marshaling and is parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "scenarios")
public class ScenarioList implements Parcelable {

    public static final Parcelable.Creator<ScenarioList> CREATOR
            = new Parcelable.Creator<ScenarioList>() {
        public ScenarioList createFromParcel(Parcel in) {
            return new ScenarioList(in);
        }

        public ScenarioList[] newArray(int size) {
            return new ScenarioList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Scenario> scenarios;

    public ScenarioList() {
    }

    public ScenarioList(Parcel in) {
        scenarios = new ArrayList<Scenario>();
        in.readTypedList(scenarios, Scenario.CREATOR);
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public boolean isAvailable() {
        return scenarios != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(scenarios);
    }
}
