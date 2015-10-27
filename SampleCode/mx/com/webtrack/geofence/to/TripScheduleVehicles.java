package mx.com.webtrack.qbo.to;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TripScheduleVehicles entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "trip_schedule_vehicles", schema = "dbo")
public class TripScheduleVehicles implements java.io.Serializable {
	private static final long serialVersionUID = 7327675808151722367L;

	// Fields
	private Integer id;
	private Schedules schedules;
	private Vehiculo vehiculo;
	private Trips trips;

	// Constructors

	/** default constructor */
	public TripScheduleVehicles() {
	}

	/** full constructor */
	public TripScheduleVehicles(Integer id, Schedules schedules,
			Vehiculo vehiculo, Trips trips) {
		this.id = id;
		this.schedules = schedules;
		this.vehiculo = vehiculo;
		this.trips = trips;
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
	@JoinColumn(name = "id_schedule", nullable = false)
	public Schedules getSchedules() {
		return this.schedules;
	}

	public void setSchedules(Schedules schedules) {
		this.schedules = schedules;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_vehicle", nullable = false)
	public Vehiculo getVehiculo() {
		return this.vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_trip", nullable = false)
	public Trips getTrips() {
		return this.trips;
	}

	public void setTrips(Trips trips) {
		this.trips = trips;
	}

}