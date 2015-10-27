package mx.com.webtrack.qbo.webservices.rest.vo;

import javax.xml.bind.annotation.XmlRootElement;

import mx.com.webtrack.qbo.webservices.vo.WSRqstTripVo;

@XmlRootElement
public class WSRqstTripsXML extends WSRqstTripVo {
	private static final long serialVersionUID = 3313868845332562880L;
	
	private UserWSXML userWS;
	private UserXML userQry;
	
	public WSRqstTripsXML(){
	
	}
	
	public WSRqstTripsXML(WSRqstTripVo wsRqstTripVo){
		this.setTrip(wsRqstTripVo.getTrip());
		this.setTripScheduleVehiclesList(wsRqstTripVo.getTripScheduleVehiclesList());
	}
	
	public UserWSXML getUserWS() {
		return userWS;
	}
	public void setUserWS(UserWSXML userWS) {
		this.userWS = userWS;
	}
	public UserXML getUserQry() {
		return userQry;
	}
	public void setUserQry(UserXML userQry) {
		this.userQry = userQry;
	}	
}
