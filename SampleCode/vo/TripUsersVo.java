package mx.com.webtrack.qbo.webservices.vo;

import java.io.Serializable;

import mx.com.webtrack.qbo.to.TripUsers;

public class TripUsersVo implements Serializable {
	private static final long serialVersionUID = 373738272952223462L;
	
	private Integer id;
	private UserVo user;
	private Integer trip;
	
	public TripUsersVo(){
	}
	
	public TripUsersVo(TripUsers tripUsers) {
		this.id = tripUsers.getId();
		this.user = new UserVo(tripUsers.getUsuario());
		this.trip = tripUsers.getTrips().getId();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserVo getUser() {
		return user;
	}
	public void setUser(UserVo user) {
		this.user = user;
	}
	public Integer getTrip() {
		return trip;
	}
	public void setTrip(Integer trip) {
		this.trip = trip;
	}
	
	@Override
	public String toString() {
		return "TripUsersVo [id=" + id + ", user=" + user + ", trip=" + trip + "]";
	}
}
