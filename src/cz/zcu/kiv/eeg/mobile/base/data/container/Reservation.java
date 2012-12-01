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
 * 
 */
public class Reservation implements Serializable {

	private static final long serialVersionUID = 8850665675446609744L;
	private int reservationId;
	private CharSequence researchGroup;
	private int researchGroupId;
	private Date fromTime;
	private Date toTime;
	private String creatorName;
	private String email;
	private boolean canRemove;

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

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

	public CharSequence getResearchGroup() {
		return researchGroup;
	}

	public void setResearchGroup(CharSequence researchGroup) {
		this.researchGroup = researchGroup;
	}

	public int getResearchGroupId() {
		return researchGroupId;
	}

	public void setResearchGroupId(int researchGroupId) {
		this.researchGroupId = researchGroupId;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public String getStringFromTime() {
		return sf.format(fromTime);
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public String getStringToTime() {
		return sf.format(toTime);
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	public boolean getCanRemove() {
		return canRemove;
	}

	public void setReservationId(int reservationId){
		this.reservationId = reservationId;
	}
	
	public int getReservationId() {
		return reservationId;
	}

}
