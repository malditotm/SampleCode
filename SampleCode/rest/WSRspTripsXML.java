package mx.com.webtrack.qbo.webservices.rest.vo;

import javax.xml.bind.annotation.XmlRootElement;

import mx.com.webtrack.qbo.webservices.vo.WSRspTripsVo;

@XmlRootElement
public class WSRspTripsXML extends WSRspTripsVo {
	private static final long serialVersionUID = 1454989781819792478L;

	public WSRspTripsXML(){
	
	}
	
	public WSRspTripsXML(WSRspTripsVo wsRspTripsVo){
		this.setResponse(wsRspTripsVo.getResponse());
		this.setTripsList(wsRspTripsVo.getTripsList());
		this.setTripUsersList(wsRspTripsVo.getTripUsersList());
		this.setTripScheduleVehiclesList(wsRspTripsVo.getTripScheduleVehiclesList());
	}
}
