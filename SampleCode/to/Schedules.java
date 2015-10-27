package mx.com.webtrack.qbo.to;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
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
 * Schedules entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "schedules", schema = "dbo")
public class Schedules implements java.io.Serializable {
	private static final long serialVersionUID = -35687724409589517L;

	// Fields
	private Integer id;
	private ScheduleStatus scheduleStatus;
	private FrecuencyTypes frecuencyTypes;
	private Usuario usuario;
	private Time startTime;
	private Time endTime;
	private Double duration;
	private Integer frecuencyValue;
	private Integer loopTimes;
	private Date startDate;
	private Date endDate;
	private Date creationDate;
	private Set<SchedulesDetail> schedulesDetails = new HashSet<SchedulesDetail>(0);
	private Set<TripScheduleVehicles> tripScheduleVehicles = new HashSet<TripScheduleVehicles>(0);

	// Constructors

	/** default constructor */
	public Schedules() {
	}

	/** minimal constructor */
	public Schedules(Integer id, ScheduleStatus scheduleStatus,
			FrecuencyTypes frecuencyTypes, Double duration,
			Date creationDate) {
		this.id = id;
		this.scheduleStatus = scheduleStatus;
		this.frecuencyTypes = frecuencyTypes;
		this.duration = duration;
		this.creationDate = creationDate;
	}

	/** full constructor */
	public Schedules(Integer id, ScheduleStatus scheduleStatus,
			FrecuencyTypes frecuencyTypes, Usuario usuario, Time startTime,
			Time endTime, Double duration, Integer frecuencyValue,
			Integer loopTimes, Timestamp startDate, Timestamp endDate,
			Timestamp creationDate, Set<SchedulesDetail> schedulesDetails,
			Set<TripScheduleVehicles> tripScheduleVehicles) {
		this.id = id;
		this.scheduleStatus = scheduleStatus;
		this.frecuencyTypes = frecuencyTypes;
		this.usuario = usuario;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.frecuencyValue = frecuencyValue;
		this.loopTimes = loopTimes;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creationDate = creationDate;
		this.schedulesDetails = schedulesDetails;
		this.tripScheduleVehicles = tripScheduleVehicles;
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
	@JoinColumn(name = "id_schedule_status", nullable = false)
	public ScheduleStatus getScheduleStatus() {
		return this.scheduleStatus;
	}

	public void setScheduleStatus(ScheduleStatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_frecuency_type", nullable = false)
	public FrecuencyTypes getFrecuencyTypes() {
		return this.frecuencyTypes;
	}

	public void setFrecuencyTypes(FrecuencyTypes frecuencyTypes) {
		this.frecuencyTypes = frecuencyTypes;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_creation_user")
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "start_time")
	public Time getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time")
	public Time getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	@Column(name = "duration", nullable = false, precision = 20, scale = 8)
	public Double getDuration() {
		return this.duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	@Column(name = "frecuency_value")
	public Integer getFrecuencyValue() {
		return this.frecuencyValue;
	}

	public void setFrecuencyValue(Integer frecuencyValue) {
		this.frecuencyValue = frecuencyValue;
	}

	@Column(name = "loop_times")
	public Integer getLoopTimes() {
		return this.loopTimes;
	}

	public void setLoopTimes(Integer loopTimes) {
		this.loopTimes = loopTimes;
	}

	@Column(name = "start_date", length = 23)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date", length = 23)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "creation_date", nullable = false, length = 23)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "schedules")
	public Set<SchedulesDetail> getSchedulesDetails() {
		return this.schedulesDetails;
	}

	public void setSchedulesDetails(Set<SchedulesDetail> schedulesDetails) {
		this.schedulesDetails = schedulesDetails;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "schedules")
	public Set<TripScheduleVehicles> getTripScheduleVehicles() {
		return this.tripScheduleVehicles;
	}

	public void setTripScheduleVehicles(Set<TripScheduleVehicles> tripScheduleVehicles) {
		this.tripScheduleVehicles = tripScheduleVehicles;
	}

}