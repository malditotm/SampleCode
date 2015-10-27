package mx.com.webtrack.qbo.webservices.soap;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import mx.com.webtrack.qbo.to.WebserviceType;
import mx.com.webtrack.qbo.webservices.mb.VehiclesMb;
import mx.com.webtrack.qbo.webservices.utils.ConstantsWS;
import mx.com.webtrack.qbo.webservices.utils.Utils;
import mx.com.webtrack.qbo.webservices.vo.UserVo;
import mx.com.webtrack.qbo.webservices.vo.UserWSVo;
import mx.com.webtrack.qbo.webservices.vo.WSRspVehiclesVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebService
public class VehiclesWS {
	@Resource
	WebServiceContext wsContext;
	@Autowired
	VehiclesMb vehiclesMb;
	private static WebserviceType ORIGIN = new WebserviceType(ConstantsWS.WEBSERVICE_SOAP_ID, ConstantsWS.WEBSERVICE_SOAP_NAME);
	
	@WebMethod
	public WSRspVehiclesVo getUserVehicles(@WebParam(name = "userWS") UserWSVo userWS,
					@WebParam(name = "userQry") UserVo userQry){
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		userWS.setIp(Utils.getIP(wsContext));
		WSRspVehiclesVo wsResp = vehiclesMb.getUserVehicles(userWS, userQry, ORIGIN);
		return wsResp;
	}
}
