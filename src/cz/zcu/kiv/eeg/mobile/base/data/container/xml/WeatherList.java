package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for holding used weather information.
 * Used for XML marshaling and is parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "weatherList")
public class WeatherList implements Parcelable {

    public static final Creator<WeatherList> CREATOR
            = new Creator<WeatherList>() {
        public WeatherList createFromParcel(Parcel in) {
            return new WeatherList(in);
        }

        public WeatherList[] newArray(int size) {
            return new WeatherList[size];
        }
    };
    @ElementList(inline = true, required = false)
    private List<Weather> weatherList;

    public WeatherList() {
    }

    public WeatherList(Parcel in) {
        weatherList = new ArrayList<Weather>();
        in.readTypedList(weatherList, Weather.CREATOR);
    }

    public WeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public boolean isAvailable() {
        return weatherList != null && !weatherList.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(weatherList);
    }
}
