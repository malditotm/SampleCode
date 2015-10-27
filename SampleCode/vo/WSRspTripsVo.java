package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"response","tripsList","tripScheduleVehiclesList","tripUsersList"})
public class WSRspTripsVo implements Serializable{
	private static final long serialVersionUID = -8578318255790343836L;
	
	private WSResponseVo response;
	private List<TripVo> tripsList;
	private List<TripUsersVo> tripUsersList;
	private List<TripScheduleVehicleVo> tripScheduleVehiclesList;
	
	public WSRspTripsVo(){
	}
	
	public WSRspTripsVo(List<TripVo> tripsList, List<TripUsersVo> tripUsersList, List<TripScheduleVehicleVo> tripScheduleVehiclesList, WSResponseVo response) {
		this.response = response;
		this.tripsList = tripsList;
		this.tripUsersList = tripUsersList;
		this.tripScheduleVehiclesList = tripScheduleVehiclesList;
	}
	
	public WSResponseVo getResponse() {
		return response;
	}
	public void setResponse(WSResponseVo response) {
		this.response = response;
	}
	@XmlElement(name = "trip")
	public List<TripVo> getTripsList() {
		return tripsList;
	}
	public void setTripsList(List<TripVo> tripsList) {
		this.tripsList = tripsList;
	}
	@XmlElement(name = "tripUser")
	public List<TripUsersVo> getTripUsersList() {
		return tripUsersList;
	}
	public void setTripUsersList(List<TripUsersVo> tripUsersList) {
		this.tripUsersList = tripUsersList;
	}
	@XmlElement(name = "tripScheduleVehicle")
	public List<TripScheduleVehicleVo> getTripScheduleVehiclesList() {
		return tripScheduleVehiclesList;
	}
	public void setTripScheduleVehiclesList(List<TripScheduleVehicleVo> tripScheduleVehiclesList) {
		this.tripScheduleVehiclesList = tripScheduleVehiclesList;
	}

	@Override
	public String toString() {
		return "WSRspTripsVo [response=" + response.toString() + ", tripsList="
				+ tripsList + ", tripUsersList=" + tripUsersList
				+ ", tripScheduleVehiclesList=" + tripScheduleVehiclesList
				+ "]";
	}
}
