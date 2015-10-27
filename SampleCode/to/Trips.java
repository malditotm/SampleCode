package mx.com.webtrack.qbo.to;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Trips entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "trips", schema = "dbo")
public class Trips implements java.io.Serializable {
	private static final long serialVersionUID = 5944598923632250085L;

	// Fields
	private Integer id;
	private TripStatus tripStatus;
	private Usuario usuario;
	private String name;
	private String notes;
	private Double distanceTrip;
	private Double timeTrip;
	private Set<TripScheduleVehicles> tripScheduleVehicles = new HashSet<TripScheduleVehicles>(0);
	private Set<TripUsers> tripUsers = new HashSet<TripUsers>(0);
	private Set<TripDestination> tripDestinations = new HashSet<TripDestination>(0);

	// Constructors

	/** default constructor */
	public Trips() {
	}

	/** minimal constructor */
	public Trips(Integer id, TripStatus tripStatus, String name, String notes, Double distanceTrip, Double timeTrip) {
		this.id = id;
		this.tripStatus = tripStatus;
		this.name = name;
		this.notes = notes;
		this.distanceTrip = distanceTrip;
		this.timeTrip = timeTrip;
	}

	/** full constructor */
	public Trips(Integer id, TripStatus tripStatus, Usuario usuario,
			String name, String notes, Double distanceTrip, Double timeTrip, Set<TripScheduleVehicles> tripScheduleVehicles, 
			Set<TripDestination> tripDestinations, Set<TripUsers> tripUsers) {
		this.id = id;
		this.tripStatus = tripStatus;
		this.usuario = usuario;
		this.name = name;
		this.notes = notes;
		this.tripDestinations = tripDestinations;
		this.tripUsers = tripUsers;
		this.tripScheduleVehicles = tripScheduleVehicles;
		this.distanceTrip = distanceTrip;
		this.timeTrip = timeTrip;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_trip_status", nullable = false)
	public TripStatus getTripStatus() {
		return this.tripStatus;
	}

	public void setTripStatus(TripStatus tripStatus) {
		this.tripStatus = tripStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_creation_user")
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "notes", nullable = false, length = 500)
	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Column(name = "distance_trip", precision = 5, scale = 2)
	public Double getDistanceTrip() {
		return distanceTrip;
	}

	public void setDistanceTrip(Double distanceTrip) {
		this.distanceTrip = distanceTrip;
	}

	@Column(name = "time_trip", precision = 5, scale = 2)
	public Double getTimeTrip() {
		return timeTrip;
	}

	public void setTimeTrip(Double timeTrip) {
		this.timeTrip = timeTrip;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "trips")
	public Set<TripDestination> getTripDestinations() {
		return this.tripDestinations;
	}

	public void setTripDestinations(Set<TripDestination> tripDestinations) {
		this.tripDestinations = tripDestinations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "trips")
	public Set<TripUsers> getTripUsers() {
		return this.tripUsers;
	}

	public void setTripUsers(Set<TripUsers> tripUsers) {
		this.tripUsers = tripUsers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "trips")
	public Set<TripScheduleVehicles> getTripScheduleVehicles() {
		return this.tripScheduleVehicles;
	}

	public void setTripScheduleVehicles(Set<TripScheduleVehicles> tripScheduleVehicles) {
		this.tripScheduleVehicles = tripScheduleVehicles;
	}

	@Override
	public String toString() {
		return "Trips [id=" + id + ", tripStatus=" + tripStatus + ", usuario="
				+ usuario + ", name=" + name + ", notes=" + notes
				+ ", distanceTrip=" + distanceTrip + ", timeTrip=" + timeTrip
				+ ", tripScheduleVehicles=" + tripScheduleVehicles
				+ ", tripUsers=" + tripUsers + ", tripDestinations="
				+ tripDestinations + "]";
	}
	
	public String toStringInfo() {
		return "Trips [id=" + id + ", tripStatus=" + tripStatus + ", usuario="
				+ usuario + ", name=" + name + ", notes=" + notes
				+ ", distanceTrip=" + distanceTrip + ", timeTrip=" + timeTrip + "]";
	}
}