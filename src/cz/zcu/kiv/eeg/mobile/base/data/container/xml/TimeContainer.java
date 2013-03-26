package cz.zcu.kiv.eeg.mobile.base.data.container.xml;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Helper container for holding date information.
 * Parcelable.
 *
 * @author Petr Miko
 */
@Root(name = "time")
public class TimeContainer implements Parcelable {

    public static final Parcelable.Creator<TimeContainer> CREATOR
            = new Parcelable.Creator<TimeContainer>() {
        public TimeContainer createFromParcel(Parcel in) {
            return new TimeContainer(in);
        }

        public TimeContainer[] newArray(int size) {
            return new TimeContainer[size];
        }
    };
    @Element
    private int second;
    @Element
    private int minute;
    @Element
    private int hour;
    @Element
    private int day;
    @Element
    private int month;
    @Element
    private int year;

    /**
     * Creates Time container and sets its field to time of creation;
     */
    public TimeContainer() {

        Time time = new Time();
        time.setToNow();

        second = time.second;
        minute = time.minute;
        hour = time.hour;
        day = time.monthDay;
        //due to java date lib
        month = time.month + 1;
        year = time.year;
    }

    public TimeContainer(Parcel in) {
        second = in.readInt();
        minute = in.readInt();
        hour = in.readInt();
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();

    }

    public TimeContainer(TimeContainer originalDate) {
        second = originalDate.getSecond();
        minute = originalDate.getMinute();
        hour = originalDate.getHour();
        day = originalDate.getDay();
        month = originalDate.getMonth();
        year = originalDate.getYear();
    }

    public TimeContainer(String time, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Calendar c = Calendar.getInstance();
        c.setTime(formatter.parse(time));

        second = c.get(Calendar.SECOND);
        minute = c.get(Calendar.MINUTE);
        hour = c.get(Calendar.HOUR_OF_DAY);
        day = c.get(Calendar.DAY_OF_MONTH);
        //+1 due to months start from 0 not 1
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets time in android.text.format.Time format.
     */
    public Time getTime() {
        Time time = new Time();
        time.set(second, minute, hour, day, month, year);
        return time;
    }

    public String toDateString() {
        return String.format("%02d.%02d.%04d", day, month, year);
    }

    public String toTimeString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public String toString() {
        return String.format("%02d.%02d.%04d %02d:%02d", day, month, year, hour, minute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(second);
        dest.writeInt(minute);
        dest.writeInt(hour);
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
    }
}
