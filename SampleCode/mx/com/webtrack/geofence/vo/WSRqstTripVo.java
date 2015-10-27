package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;
import java.util.List;

public class WSRqstTripVo implements Serializable{
	private static final long serialVersionUID = -4929890790514519592L;
	
	private TripVo trip;
	private List<TripScheduleVehicleVo> tripScheduleVehiclesList;
	
	public TripVo getTrip() {
		return trip;
	}
	public void setTrip(TripVo trip) {
		this.trip = trip;
	}
	public List<TripScheduleVehicleVo> getTripScheduleVehiclesList() {
		return tripScheduleVehiclesList;
	}
	public void setTripScheduleVehiclesList(List<TripScheduleVehicleVo> tripScheduleVehiclesList) {
		this.tripScheduleVehiclesList = tripScheduleVehiclesList;
	}
	@Override
	public String toString() {
		return "WSRqstTripVo [trip=" + trip + ", tripScheduleVehiclesList="
				+ tripScheduleVehiclesList + "]";
	}
}
