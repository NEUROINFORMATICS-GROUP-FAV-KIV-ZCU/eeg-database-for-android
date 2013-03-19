package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Data container for holding experiments and their information.
 *
 * @author Petr Miko
 */
@Root(name = "experiments")
public class ExperimentList implements Parcelable {

    @ElementList(inline = true, required = false)
    private List<Experiment> experiments;

    public ExperimentList() {
    }

    public ExperimentList(Parcel in) {
        experiments = new ArrayList<Experiment>();
        in.readTypedList(experiments, Experiment.CREATOR);
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public void setGroups(List<Experiment> experiments) {
        this.experiments = experiments;
    }

    public boolean isAvailable() {
        return experiments != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(experiments);
    }
}