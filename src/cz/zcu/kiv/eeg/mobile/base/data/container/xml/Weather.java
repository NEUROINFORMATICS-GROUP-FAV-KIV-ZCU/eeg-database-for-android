package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Data container for holding weather information.
 * Supports XML marshaling and is parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "weather")
public class Weather implements Parcelable {

    @Element
    private int weatherId;
    @Element
    private String title;
    @Element(required = false)
    private String description;

    public Weather() {
    }

    public Weather(Parcel in) {
        weatherId = in.readInt();
        title = in.readString();
        description = in.readString();
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(weatherId);
        dest.writeString(title);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<Weather> CREATOR
            = new Parcelable.Creator<Weather>() {
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
