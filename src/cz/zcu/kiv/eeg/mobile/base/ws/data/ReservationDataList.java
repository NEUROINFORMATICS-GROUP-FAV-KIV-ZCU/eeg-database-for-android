package cz.zcu.kiv.eeg.mobile.base.ws.data;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "reservations")
public class ReservationDataList {

	@ElementList(inline = true, required = false)
	private List<ReservationData> reservations;

	public List<ReservationData> getReservations() {
		return reservations;
	}

	public void setReservations(List<ReservationData> reservations) {
		this.reservations = reservations;
	}
}
