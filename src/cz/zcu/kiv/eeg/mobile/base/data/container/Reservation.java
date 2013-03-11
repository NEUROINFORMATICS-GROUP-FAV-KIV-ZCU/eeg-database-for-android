package cz.zcu.kiv.eeg.mobile.base.data.container;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data container for reservation information.
 *
 * @author Petr Miko
 */
public class Reservation implements Serializable {

    private static final long serialVersionUID = 8850665675446609744L;
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private int reservationId;
    private CharSequence researchGroup;
    private int researchGroupId;
    private Date fromTime;
    private Date toTime;
    private String creatorName;
    private String email;
    private boolean canRemove;

    /**
     * Constructor for reservation data container.
     *
     * @param reservationId   reservation identifier
     * @param researchGroup   research group name
     * @param researchGroupId research group identifier
     * @param fromTime        reservation start time
     * @param toTime          reservation end time
     * @param creatorName     name of person who created reservation
     * @param email           creator's email
     * @param canRemove       user can remove reservation
     * @throws ParseException error occurred during parsing time strings
     */
    public Reservation(int reservationId, CharSequence researchGroup, int researchGroupId, String fromTime, String toTime, String creatorName, String email,
                       boolean canRemove) throws ParseException {
        this.reservationId = reservationId;
        this.researchGroup = researchGroup;
        this.researchGroupId = researchGroupId;
        this.fromTime = sf.parse(fromTime);
        this.toTime = sf.parse(toTime);
        this.creatorName = creatorName;
        this.email = email;
        this.canRemove = canRemove;
    }

    /**
     * Constructor for reservation data container.
     *
     * @param reservationId   reservation identifier
     * @param researchGroup   research group name
     * @param researchGroupId research group identifier
     * @param fromTime        reservation start time
     * @param toTime          reservation end time
     * @param creatorName     name of person who created reservation
     * @param email           creator's email
     * @param canRemove       user can remove reservation
     */
    public Reservation(int reservationId, CharSequence researchGroup, int researchGroupId, Date fromTime, Date toTime, String creatorName, String email,
                       boolean canRemove) {
        this.reservationId = reservationId;
        this.researchGroup = researchGroup;
        this.researchGroupId = researchGroupId;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.creatorName = creatorName;
        this.email = email;
        this.canRemove = canRemove;
    }

    /**
     * Research group name getter.
     *
     * @return research group name
     */
    public CharSequence getResearchGroup() {
        return researchGroup;
    }

    /**
     * Research group name setter.
     *
     * @param researchGroup group name
     */
    public void setResearchGroup(CharSequence researchGroup) {
        this.researchGroup = researchGroup;
    }

    /**
     * Research group identifier getter.
     *
     * @return research group identifier
     */
    public int getResearchGroupId() {
        return researchGroupId;
    }

    /**
     * Research group identifier setter.
     *
     * @param researchGroupId research group identifier
     */
    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    /**
     * Getter of reservation start time.
     *
     * @return reservation start time
     */
    public Date getFromTime() {
        return fromTime;
    }

    /**
     * Setter of reservation start time.
     *
     * @param fromTime reservation start time
     */
    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    /**
     * Getter of reservation start time in string representation.
     *
     * @return reservation start time
     */
    public String getStringFromTime() {
        return sf.format(fromTime);
    }

    /**
     * Getter of reservation end time.
     *
     * @return reservation end time
     */
    public Date getToTime() {
        return toTime;
    }

    /**
     * Setter of reservation end time.
     *
     * @param toTime reservation end time
     */
    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    /**
     * Getter of reservation end time in string representation.
     *
     * @return reservation end time
     */
    public String getStringToTime() {
        return sf.format(toTime);
    }

    /**
     * Getter of reservation creator's name.
     *
     * @return creator's name
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * Setter of reservation creator's name.
     *
     * @param creatorName creator's name
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * Getter of creator's email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter of creator's email.
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checker, whether can user remove reservation.
     *
     * @return can user remove reservation
     */
    public boolean isCanRemove() {
        return canRemove;
    }

    /**
     * Setter, whether can user remove reservation.
     *
     * @param canRemove can user remove reservation
     */
    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }

    /**
     * Getter of reservation identifier.
     *
     * @return reservation identifier
     */
    public int getReservationId() {
        return reservationId;
    }

    /**
     * Setter of reservation identifier.
     *
     * @param reservationId reservation identifier
     */
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

}
