package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import mx.com.webtrack.qbo.to.TripDestination;
import mx.com.webtrack.qbo.to.TripStatus;
import mx.com.webtrack.qbo.to.Trips;

@XmlType(propOrder={"id","name","notes","tripStatus","destinationList"})
public class TripVo implements Serializable {
	private static final long serialVersionUID = 3967976519875113732L;

	private Integer id;
	private Integer tripStatus;
	private String name;
	private String notes;
	private List<DestinationVo> destinationList;
	
	public TripVo(){
		
	}
	
	public TripVo(Trips trip){
		this.id = trip.getId();
		this.tripStatus = trip.getTripStatus().getId();
		this.name = trip.getName();
		this.notes = trip.getNotes();
		List<DestinationVo> destinationVoList = new ArrayList<DestinationVo>();
		for(TripDestination tripDestination: trip.getTripDestinations()){
				destinationVoList.add(new DestinationVo(tripDestination.getDestination()));
		}
		this.destinationList = destinationVoList;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTripStatus() {
		return tripStatus;
	}
	public void setTripStatus(Integer tripStatus) {
		this.tripStatus = tripStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@XmlElement(name = "destination")
	public List<DestinationVo> getDestinationList() {
		return destinationList;
	}
	public void setDestinationList(List<DestinationVo> destinationList) {
		this.destinationList = destinationList;
	}

	public Trips generateTo(){
		Trips trip = new Trips();
		trip.setId(id);
		trip.setName(name);
		trip.setNotes(notes);
		TripStatus tripStatus = new TripStatus();
		tripStatus.setId(this.tripStatus);
		trip.setTripStatus(tripStatus);
		return trip;
	}
	
	public boolean hasId() {
		if(id == null){
			return false;
		}
		return true;
	}

	public boolean hasValidFields() {
		if(tripStatus == null || tripStatus < 1 || tripStatus > 2){
			return false;
		}
		if(name == null || name.trim().equals("null") || name.trim().equals("")){
			return false;
		}
		if(notes == null || notes.trim().equals("null") || notes.trim().equals("")){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TripVo [id=" + id + ", tripStatus=" + tripStatus + ", name="
				+ name + ", notes=" + notes + ", destinationList="
				+ destinationList + "]";
	}
}
