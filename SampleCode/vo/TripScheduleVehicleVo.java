package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

import mx.com.webtrack.qbo.to.Schedules;
import mx.com.webtrack.qbo.to.TripScheduleVehicles;
import mx.com.webtrack.qbo.to.Trips;
import mx.com.webtrack.qbo.to.Vehiculo;

@XmlType(propOrder={"tripId","scheduleId","vehicleId"})
public class TripScheduleVehicleVo implements Serializable {
	private static final long serialVersionUID = -2408737268810117519L;
	
	private Integer tripId;
	private Integer scheduleId;
	private Integer vehicleId;
	
	public TripScheduleVehicleVo(){
		
	}
	
	public TripScheduleVehicleVo(TripScheduleVehicles tripScheduleVehicle) {
		this.tripId = tripScheduleVehicle.getTrips().getId();
		this.scheduleId = tripScheduleVehicle.getSchedules().getId();
		this.vehicleId = tripScheduleVehicle.getVehiculo().getIdVehiculo();
	}

	public Integer getTripId() {
		return tripId;
	}
	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public TripScheduleVehicles generateTo(Trips trip, Schedules schedule, Vehiculo vehicle){
		TripScheduleVehicles tripScheduleVehicle = new TripScheduleVehicles();
		tripScheduleVehicle.setTrips(trip);
		tripScheduleVehicle.setSchedules(schedule);
		tripScheduleVehicle.setVehiculo(vehicle);
		return tripScheduleVehicle;
	}

	@Override
	public String toString() {
		return "TripScheduleVehicleVo [tripId=" + tripId + ", scheduleId="
				+ scheduleId + ", vehicleId=" + vehicleId + "]";
	}
}
