package mx.com.webtrack.qbo.webservices.mb;

import java.util.Date;
import java.util.List;

import mx.com.webtrack.qbo.to.UserWebservices;
import mx.com.webtrack.qbo.to.Usuario;
import mx.com.webtrack.qbo.to.WebserviceRequest;
import mx.com.webtrack.qbo.to.WebserviceType;
import mx.com.webtrack.qbo.webservices.bo.UsersBo;
import mx.com.webtrack.qbo.webservices.bo.VehiclesBo;
import mx.com.webtrack.qbo.webservices.bo.WebservicesBo;
import mx.com.webtrack.qbo.webservices.utils.ConstantsWS;
import mx.com.webtrack.qbo.webservices.utils.Utils;
import mx.com.webtrack.qbo.webservices.vo.UserVo;
import mx.com.webtrack.qbo.webservices.vo.UserWSVo;
import mx.com.webtrack.qbo.webservices.vo.ValidUserVo;
import mx.com.webtrack.qbo.webservices.vo.VehicleVo;
import mx.com.webtrack.qbo.webservices.vo.WSResponseVo;
import mx.com.webtrack.qbo.webservices.vo.WSRspVehiclesVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service("vehiclesMb")
public class VehiclesMb {
	@Autowired
	UsersBo usersBo;
	@Autowired
	UsersMb usersMb;
	@Autowired
	VehiclesBo vehiclesBo;
	@Autowired
	WebservicesBo webservicesBo;
	boolean errorFlag;
	Integer error = 0;
	
	static int SUCCESS = 0;
	static int SERVER_ERROR = 1;
	static int METHOD_NOT_IMPLEMENTED = 2;
	static int PROHIBITED_ACTION = 3;
	static int NO_USERNAME = 4;
	static int NO_PASSWORD = 5;
	static int WRONG_USERNAME_OR_PASSWORD = 6;
	static int WRONG_QUERIED_USER = 7;
	static int NO_VEHICLES = 8;
	
	static String[] RESPONSE_DESCRIPTIONS = { 
			"Success",
			"Server error",
			"This method is not implemented",
			"You can not modify that information",
			"No username present",
			"No password present",
			"User and/or password wrong",
			"The query user data provided is not valid",
			"No vehicles found"
		};
	
	public WSRspVehiclesVo getUserVehicles(UserWSVo userWS, UserVo userQry, WebserviceType origin){
		//System.out.println(origin.getWebserviceType() + "@VehiclesWS@getUserVehicles requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString() + "@" + userQry.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("VehiclesWS");
		webserviceRequest.setWebserviceFunctionRequested("getUserVehicles");
		
		errorFlag = false;
		error = SUCCESS;
		
		WSResponseVo response = new WSResponseVo();
		WSRspVehiclesVo wsResp = new WSRspVehiclesVo();
		try{
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			
			UserWebservices userWebservices = null;
			// UserWS log in 
			ValidUserVo validUserVo = usersMb.validateWSUser(userWS, userWebservices, "VehiclesWS@getUserVehicles");
			errorFlag = validUserVo.isErrorFlag();
			error = validUserVo.getErrorCode();
			if(validUserVo.isValidUser()){
				userWebservices = validUserVo.getUserWebservices();
				webserviceRequest.setUserWebservices(userWebservices);
				if(userQry.getUserId() == null || userQry.getUserId() <= 0){
					errorFlag = true;
					userQry.setUserValidFlag(false);
					userQry.setUserValidString("There is no query user id");
				}
				if(userQry.getUser() == null || userQry.getUser().trim().equals("null") || userQry.getUser().trim().equals("")){
					errorFlag = true;
					userQry.setUserValidFlag(false);
					userQry.setUserValidString("There is no query user name");
				}
				
				if(!errorFlag){
					List<VehicleVo> vehiclesList = null;
					Usuario validUser = usersBo.validateUser(userQry.getUserId());
					if(validUser != null && validUser.getUsuario().equals(userQry.getUser())){
						vehiclesList = vehiclesBo.getUserVehicles(userQry.getUserId());
						//System.out.println(vehiclesList.size());
					}
					else{
						userQry.setUserValidFlag(false);
						userQry.setUserValidString("Los datos del usuario no son validos");
						error = WRONG_QUERIED_USER;
					}
					wsResp.setUserVehiclesList(vehiclesList);
					//System.out.println("return users vehicles");
				}
				else{
					error = WRONG_QUERIED_USER;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			error = SERVER_ERROR;
			
		}
		
		response.setCode(error);
		response.setDescription(RESPONSE_DESCRIPTIONS[error]);
		if(!userQry.isUserValidFlag() && userQry.getUserValidString() != null){
			response.setDescription(response.getDescription() + "@" + userQry.getUserValidString());
		}
		
		wsResp.setResponse(response);

		webserviceRequest.setResponseDate(new Date());
		webserviceRequest.setResponse(wsResp.toString());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}

}
