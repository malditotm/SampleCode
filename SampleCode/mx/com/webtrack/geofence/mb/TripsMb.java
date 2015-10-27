package mx.com.webtrack.qbo.webservices.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import mx.com.webtrack.qbo.to.Destination;
import mx.com.webtrack.qbo.to.GeofencesRoute;
import mx.com.webtrack.qbo.to.Schedules;
import mx.com.webtrack.qbo.to.TripDestination;
import mx.com.webtrack.qbo.to.TripScheduleVehicles;
import mx.com.webtrack.qbo.to.TripStatus;
import mx.com.webtrack.qbo.to.Trips;
import mx.com.webtrack.qbo.to.UserWebservices;
import mx.com.webtrack.qbo.to.Vehiculo;
import mx.com.webtrack.qbo.to.WebserviceRequest;
import mx.com.webtrack.qbo.to.WebserviceType;
import mx.com.webtrack.qbo.webservices.bo.GeofencesRouteBo;
import mx.com.webtrack.qbo.webservices.bo.SchedulesBo;
import mx.com.webtrack.qbo.webservices.bo.TripsBo;
import mx.com.webtrack.qbo.webservices.bo.UsersBo;
import mx.com.webtrack.qbo.webservices.bo.VehiclesBo;
import mx.com.webtrack.qbo.webservices.bo.WebservicesBo;
import mx.com.webtrack.qbo.webservices.utils.ConstantsWS;
import mx.com.webtrack.qbo.webservices.utils.Utils;
import mx.com.webtrack.qbo.webservices.vo.DestinationVo;
import mx.com.webtrack.qbo.webservices.vo.TripScheduleVehicleVo;
import mx.com.webtrack.qbo.webservices.vo.TripUsersVo;
import mx.com.webtrack.qbo.webservices.vo.TripVo;
import mx.com.webtrack.qbo.webservices.vo.UserWSVo;
import mx.com.webtrack.qbo.webservices.vo.ValidUserVo;
import mx.com.webtrack.qbo.webservices.vo.WSResponseVo;
import mx.com.webtrack.qbo.webservices.vo.WSRqstTripVo;
import mx.com.webtrack.qbo.webservices.vo.WSRspTripsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service("tripsMb")
public class TripsMb {
	@Autowired
	GeofencesRouteBo geofencesRouteBo;
	@Autowired
	UsersBo usersBo;
	@Autowired
	UsersMb usersMb;
	@Autowired
	TripsBo tripsBo;
	@Autowired
	SchedulesBo schedulesBo;
	@Autowired
	VehiclesBo vehiclesBo;
	@Autowired
	WebservicesBo webservicesBo;
	boolean errorFlag;
	Integer error = 0;
	String procedure;
	

	static int SUCCESS = 0;
	static int SERVER_ERROR = 1;
	static int METHOD_NOT_IMPLEMENTED = 2;
	static int PROHIBITED_ACTION = 3;
	static int NO_USERNAME = 4;
	static int NO_PASSWORD = 5;
	static int WRONG_USERNAME_OR_PASSWORD = 6;
	static int NO_TRIPS = 7;
	static int TRIP_NOT_VALID = 8;
	static int TRIP_ID_NOT_VALID = 9;
	static int TRIP_HAS_NOT_VALID_FIELDS = 10;
	static int TRIP_DESTINATIONS_NUMBER_NOT_CORRECT = 11;
	static int TRIP_POINTS_NOT_CORRECT = 12;
	static int TRIP_NAME_ALREADY_USED = 13;
	static int TRIP_CAN_NOT_BE_SAVED = 14;
	static int RELATIONS_LIST_EMPTY = 15;
	static int TRIP_ID_EMPTY = 16;
	static int SCHEDULE_ID_EMPTY = 17;
	static int VEHICLE_ID_EMPTY = 18;
	static int EXISTENT_RELATIONS = 19;
	static int INEXISTENT_RELATIONS = 20;
	
	static String[] RESPONSE_DESCRIPTIONS = { 
			"Success",
			"Server error",
			"This method is not implemented",
			"You can not modify that information",
			"No username present",
			"No password present",
			"User and/or password wrong",
			"No trips found",
			"Trip is not valid for this action",
			"Trip Id is not valid for this action",
			"Trip fields are not filled",
			"The number of destinations is not correct",
			"The points are not correct",
			"A trip with that name was already saved",
			"The trip was not correct to save",
			"The relations list is empty",
			"The trip id must not be empty",
			"The schedule id must not be empty",
			"The vehicle id must not be empty",
			"Some relations already were created before"
		};

	public WSRspTripsVo getAllTrips(UserWSVo userWS, WebserviceType origin) {
		//System.out.println(origin.getWebserviceType() + "@TripsWS@getAllTrips requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("TripsWS");
		webserviceRequest.setWebserviceFunctionRequested("getAllTrips");		
		
		errorFlag = false;
		error = SUCCESS;
		
		WSResponseVo response = new WSResponseVo();
		List<Trips> tripsList;
		List<TripVo> tripsVoList = new ArrayList<TripVo>();
		List<TripUsersVo> tripUsersList = new ArrayList<TripUsersVo>();
		List<TripScheduleVehicleVo> tripScheduleVehiclesVoList= new ArrayList<TripScheduleVehicleVo>();
		try{
			//System.out.println(userWS.getUser() + " :: " + userWS.getPassword());
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			
			UserWebservices userWebservices = null;
			// UserWS Log in
			ValidUserVo validUserVo = usersMb.validateWSUser(userWS, userWebservices, "TripsWS@getAllTrips");
			errorFlag = validUserVo.isErrorFlag();
			error = validUserVo.getErrorCode();
			if(validUserVo.isValidUser()){
				userWebservices = validUserVo.getUserWebservices();
				webserviceRequest.setUserWebservices(userWebservices);
				tripsList = tripsBo.getTrips(userWebservices.getUsuario().getIdUsuario());
				List<Trips> tripsTmpList = new ArrayList<Trips>();
				List<TripScheduleVehicles> tripScheduleVehiclesTmpList = new ArrayList<TripScheduleVehicles>();
				if(tripsList != null && !tripsList.isEmpty()){
					for(Trips trip: tripsList){
						if(!tripsTmpList.contains(trip)){
							tripsTmpList.add(trip);
							tripsVoList.add(new TripVo(trip));
							
							Set<TripScheduleVehicles> tripScheduleVehiclesSet = trip.getTripScheduleVehicles();
							for(TripScheduleVehicles tripScheduleVehicle: tripScheduleVehiclesSet){
								if(!tripScheduleVehiclesTmpList.contains(tripScheduleVehicle)){
									tripScheduleVehiclesTmpList.add(tripScheduleVehicle);
									tripScheduleVehiclesVoList.add(new TripScheduleVehicleVo(tripScheduleVehicle));
								}
							}
							
							//System.out.println("trip destinations" + trip.getTripDestinations() +" $$ trip-chedules-vehicles:" + trip.getTripScheduleVehicles() + " $$ tripUsers:" + trip.getTripUsers() + " $$ tripStatus:" + trip.getTripStatus().getDescription());/**/
							if(trip.getTripDestinations() != null){
								//System.out.println("Destinations: " + trip.getTripDestinations().size());
							}
							if(trip.getTripScheduleVehicles() != null){
								//System.out.println("TripScheduleVehivels: " + trip.getTripScheduleVehicles().size());
							}
							if(trip.getTripUsers()!= null){
								//System.out.println("Users: " + trip.getTripUsers().size());
							}
							if(trip.getTripStatus() != null){
								//System.out.println("Status: " + trip.getTripStatus().getId());
							}
						}
					}
				}
				else{
					error = NO_TRIPS;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			error = SERVER_ERROR;
		}
		
		response.setCode(error);
		response.setDescription(RESPONSE_DESCRIPTIONS[error]);
		WSRspTripsVo wsResp = new WSRspTripsVo(tripsVoList, tripUsersList, tripScheduleVehiclesVoList, response);
		webserviceRequest.setResponseDate(new Date());
		webserviceRequest.setResponse(wsResp.toString());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}

	public WSRspTripsVo getTripByID(UserWSVo userWS, WSRqstTripVo rqstTrips, WebserviceType origin) {
		//System.out.println(origin.getWebserviceType() + "@TripsWS@getTripByID requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("TripsWS");
		webserviceRequest.setWebserviceFunctionRequested("getTripByID");		
		
		errorFlag = false;
		error = SUCCESS;
		
		WSResponseVo response = new WSResponseVo();
		List<Trips> tripsList;
		List<TripVo> tripsVoList = new ArrayList<TripVo>();
		List<TripUsersVo> tripUsersList = new ArrayList<TripUsersVo>();
		List<TripScheduleVehicleVo> tripScheduleVehiclesVoList= new ArrayList<TripScheduleVehicleVo>();
		try{
			//System.out.println(userWS.getUser() + " :: " + userWS.getPassword());
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			
			UserWebservices userWebservices = null;
			// UserWS Log in
			ValidUserVo validUserVo = usersMb.validateWSUser(userWS, userWebservices, "TripsWS@getTripByID");
			errorFlag = validUserVo.isErrorFlag();
			error = validUserVo.getErrorCode();
			if(validUserVo.isValidUser()){
				userWebservices = validUserVo.getUserWebservices();
				webserviceRequest.setUserWebservices(userWebservices);
				tripsList = tripsBo.getTripByID(userWebservices.getUsuario().getIdUsuario(), rqstTrips.getTrip().getId());
				List<Trips> tripsTmpList = new ArrayList<Trips>();
				List<TripScheduleVehicles> tripScheduleVehiclesTmpList = new ArrayList<TripScheduleVehicles>();
				if(tripsList != null && !tripsList.isEmpty()){
					for(Trips trip: tripsList){
						if(!tripsTmpList.contains(trip)){
							tripsTmpList.add(trip);
							tripsVoList.add(new TripVo(trip));
							
							Set<TripScheduleVehicles> tripScheduleVehiclesSet = trip.getTripScheduleVehicles();
							for(TripScheduleVehicles tripScheduleVehicle: tripScheduleVehiclesSet){
								if(!tripScheduleVehiclesTmpList.contains(tripScheduleVehicle)){
									tripScheduleVehiclesTmpList.add(tripScheduleVehicle);
									tripScheduleVehiclesVoList.add(new TripScheduleVehicleVo(tripScheduleVehicle));
								}
							}
							
							//System.out.println("trip destinations" + trip.getTripDestinations() +" $$ trip-chedules-vehicles:" + trip.getTripScheduleVehicles() + " $$ tripUsers:" + trip.getTripUsers() + " $$ tripStatus:" + trip.getTripStatus().getDescription());/**/
							if(trip.getTripDestinations() != null){
								//System.out.println("Destinations: " + trip.getTripDestinations().size());
							}
							if(trip.getTripScheduleVehicles() != null){
								//System.out.println("TripScheduleVehivels: " + trip.getTripScheduleVehicles().size());
							}
							if(trip.getTripUsers()!= null){
								//System.out.println("Users: " + trip.getTripUsers().size());
							}
							if(trip.getTripStatus() != null){
								//System.out.println("Status: " + trip.getTripStatus().getId());
							}
						}
					}
				}
				else{
					error = NO_TRIPS;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			error = SERVER_ERROR;
		}
		
		response.setCode(error);
		response.setDescription(RESPONSE_DESCRIPTIONS[error]);
		WSRspTripsVo wsResp = new WSRspTripsVo(tripsVoList, tripUsersList, tripScheduleVehiclesVoList, response);
		webserviceRequest.setResponseDate(new Date());
		webserviceRequest.setResponse(wsResp.toString());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}
	
	public WSRspTripsVo createTrip(UserWSVo userWS, WSRqstTripVo tripWSReqVo, WebserviceType origin) {
		//System.out.println(origin.getWebserviceType() + "@TripsWS@createTrip requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString() + "@" + tripWSReqVo.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("TripsWS");
		webserviceRequest.setWebserviceFunctionRequested("createTrip");
		
		procedure = "create";
		WSRspTripsVo wsResp = saveOrUpdateTrip(userWS, tripWSReqVo, webserviceRequest, "TripsWS@createTrip");
		webserviceRequest.setResponseDate(new Date());
		webserviceRequest.setResponse(wsResp.toString());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}
	
	public WSRspTripsVo updateTrip(UserWSVo userWS, WSRqstTripVo tripWSReqVo, WebserviceType origin) {
		//System.out.println(origin.getWebserviceType() + "@TripsWS@updateTrip requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString() + "@" + tripWSReqVo.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("TripsWS");
		webserviceRequest.setWebserviceFunctionRequested("updateTrip");

		procedure = "update";
		WSRspTripsVo wsResp = saveOrUpdateTrip(userWS, tripWSReqVo, webserviceRequest, "TripsWS@updateTrip");
		webserviceRequest.setResponseDate(new Date());
		webserviceRequest.setResponse(wsResp.toString());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}
	
	public WSRspTripsVo deleteTrip(UserWSVo userWS, WSRqstTripVo tripWSReqVo, WebserviceType origin) {
		//System.out.println(origin.getWebserviceType() + "@TripsWS@deleteteTrip requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString() + "@" + tripWSReqVo.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("TripsWS");
		webserviceRequest.setWebserviceFunctionRequested("deleteTrip");

		procedure = "delete";
		WSRspTripsVo wsResp = saveOrUpdateTrip(userWS, tripWSReqVo, webserviceRequest, "TripsWS@deleteTrip");
		webserviceRequest.setResponseDate(new Date());
		webserviceRequest.setResponse(wsResp.toString());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}
	
	private WSRspTripsVo saveOrUpdateTrip(UserWSVo userWS, WSRqstTripVo tripWSReqVo, WebserviceRequest webserviceRequest, String method){
		errorFlag = false;
		error = SUCCESS;
		
		WSResponseVo response = new WSResponseVo();
		List<TripVo> tripsVoList = new ArrayList<TripVo>();
		List<TripUsersVo> tripUsersList = new ArrayList<TripUsersVo>();
		List<TripScheduleVehicleVo> tripScheduleVehiclesList = new ArrayList<TripScheduleVehicleVo>();
		try{
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			UserWebservices userWebservices = null;
			// UserWS Log in
			ValidUserVo validUserVo = usersMb.validateWSUser(userWS, userWebservices, method);
			errorFlag = validUserVo.isErrorFlag();
			error = validUserVo.getErrorCode();
			if(validUserVo.isValidUser()){
				userWebservices = validUserVo.getUserWebservices();
				webserviceRequest.setUserWebservices(userWebservices);
				TripVo tripVo = tripWSReqVo.getTrip();
				if(tripVo != null){
					if(procedure.equals("create")){
						if(!tripVo.hasId()){
							if(tripVo.hasValidFields()){
								List<DestinationVo> destinationVoList = tripWSReqVo.getTrip().getDestinationList();
								if(destinationVoList.size() == 2){
									boolean validGeofences = true;
									List<Destination> destinationList = new ArrayList<Destination>();
									Integer order = 0;
									for(DestinationVo destinationVo: destinationVoList){
										if(destinationVo.getPoint().hasValidFields()){
											destinationList.add(destinationVo.generateTo(userWebservices.getUsuario(), order, tripVo.getName()));
											//System.out.println(destinationVo.toString());
										}
										else{
											validGeofences = false;
										}
										order++;
									}
									if(validGeofences){
										Trips trip = tripVo.generateTo();
										//System.out.println(new TripVo(trip).toString());
										trip.setUsuario(userWebservices.getUsuario());
										Trips tripOnDB = tripsBo.getTrip(trip.getName(), userWebservices.getUsuario().getIdUsuario());
										if(tripOnDB == null){
											tripsBo.saveOrUpdate(trip, destinationList, null);
											if(trip.getId() != null){
												tripsVoList.add(new TripVo(trip));
											}
											else{
												errorFlag = true;
												error = TRIP_CAN_NOT_BE_SAVED;
											}
										}
										else{
											errorFlag = true;
											error = TRIP_NAME_ALREADY_USED;
										}
									}
									else{
										errorFlag = true;
										error = TRIP_POINTS_NOT_CORRECT;
									}
								}
								else{
									errorFlag = true;
									error = TRIP_DESTINATIONS_NUMBER_NOT_CORRECT;
								}
							}
							else{
								errorFlag = true;
								error = TRIP_HAS_NOT_VALID_FIELDS;
							}
						}
						else{
							errorFlag = true;
							error = TRIP_ID_NOT_VALID;
						}
					}
					// Update Trip
					else if(procedure.equals("update")){
						if(tripVo.hasId()){
							Trips tripDB = tripsBo.getTrip(tripVo.getId());
							List<Destination> destinationDBList = new ArrayList<Destination>();
							//System.out.println("*°*°*°*°*°*°*     tripDestinations::::     *°*°*°*°*°*°*");
							for(TripDestination tripDestination: tripDB.getTripDestinations()){
								//System.out.println(tripDestination.getDestination().toString());
								destinationDBList.add(tripDestination.getDestination());
							}
							destinationDBList = reorderDestinationList(destinationDBList);
							//System.out.println("*°*°*°*°*°*°*     ::::tripDestinations     *°*°*°*°*°*°*");
							//System.out.println(" TripDB: " + tripDB.toStringInfo() + "\n TripVo: " + tripVo.toString());
							if(userWebservices.getUsuario().getIdUsuario() == tripDB.getUsuario().getIdUsuario()){
								if(tripVo.hasValidFields()){
									List<DestinationVo> destinationVoList = tripWSReqVo.getTrip().getDestinationList();
									List<GeofencesRoute> deletingGeofenceRouteList = new ArrayList<GeofencesRoute>();
									List<GeofencesRoute> updatingGeofenceRouteList = new ArrayList<GeofencesRoute>();
									if(destinationVoList.size() == 2){
										boolean validGeofences = true;
										List<Destination> destinationList = new ArrayList<Destination>();
										List<DestinationVo> validDestinationVoList = validateOrder(destinationVoList); 
										boolean validOrder;
										if(validDestinationVoList.size() == destinationVoList.size()){
											validOrder = true;
										}
										else{
											validOrder = false;
										}
										Integer order = 0;
										for(DestinationVo destinationVo: destinationVoList){
											if(destinationVo.getPoint().hasValidFields()){
												if(validOrder){
													destinationList.add(destinationVo.generateTo(userWebservices.getUsuario(), tripVo.getName()));
												}
												else{
													destinationList.add(destinationVo.generateTo(userWebservices.getUsuario(), order, tripVo.getName()));
												}
												destinationList.get(destinationList.size()-1).toString();
											}
											else{
												validGeofences = false;
											}
											order++;
										}
										for(int i = 0; i < destinationList.size(); i++){
												//if(destinationList.get(i).getId() == null){
												destinationList.get(i).setId(destinationDBList.get(i).getId());
												//}
												String geofenceRouteComparation = geofencesRouteBo.compareFields(destinationList.get(i).getGeofencesRoute(), destinationDBList.get(i).getGeofencesRoute());
												//System.out.println(geofenceRouteComparation);
												if(geofenceRouteComparation.contains("OK")){
													if(geofenceRouteComparation.contains("OK 1")){
														//System.out.println("OK with geofences route");
														destinationList.get(i).getGeofencesRoute().setId(destinationDBList.get(i).getGeofencesRoute().getId());
													}
													updatingGeofenceRouteList.add(destinationList.get(i).getGeofencesRoute());
												}
												else if(geofenceRouteComparation.contains("Fail")){
													//System.out.println("Geofences Route not correct");
													deletingGeofenceRouteList.add(destinationDBList.get(i).getGeofencesRoute());
												}
											if(i == destinationDBList.size() && destinationDBList.size() <= destinationList.size()){
												i = destinationList.size();
											}
										}
										if(validGeofences){
											Trips trip = tripVo.generateTo();
											//System.out.println(new TripVo(trip).toString());
											trip.setUsuario(userWebservices.getUsuario());
											Trips tripOnDB = tripsBo.getTrip(trip.getName(), userWebservices.getUsuario().getIdUsuario());
											// TODO validate if is the same trip (name)
											if(tripOnDB == null || tripOnDB.getId() == trip.getId()){
												tripsBo.saveOrUpdate(trip, destinationList, deletingGeofenceRouteList);
												if(trip.getId() != null){
													tripsVoList.add(new TripVo(trip));
												}
												else{
													errorFlag = true;
													error = TRIP_CAN_NOT_BE_SAVED;
												}
											}
											else{
												errorFlag = true;
												error = TRIP_NAME_ALREADY_USED;
											}
										}
										else{
											errorFlag = true;
											error = TRIP_POINTS_NOT_CORRECT;
										}
									}
									else{
										errorFlag = true;
										error = TRIP_DESTINATIONS_NUMBER_NOT_CORRECT;
									}
								}
								else{
									errorFlag = true;
									error = TRIP_HAS_NOT_VALID_FIELDS;
								}
							}
							else{
								errorFlag = true;
								error = PROHIBITED_ACTION;
							}
						}
						else{
							errorFlag = true;
							error = TRIP_ID_NOT_VALID;
						}
					}
					else if(procedure.equals("delete")){
						Trips tripDB = tripsBo.getTrip(tripVo.getId());
						if(userWS.getUserId() == tripDB.getUsuario().getIdUsuario()){
							if(tripVo.hasValidFields()){
								tripDB.setTripStatus(new TripStatus());
								tripDB.getTripStatus().setId(3);
								tripDB.setName("_ERRASED_" + tripDB.getName() + "_BY_USER" + userWS.getUser());
								tripsBo.saveOrUpdate(tripDB);
							}
						}
					}
				}
				else{
					errorFlag = true;
					// TODO renumber errors
					error = TRIP_NOT_VALID;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			error = SERVER_ERROR;
			//System.out.println(RESPONSE_DESCRIPTIONS[error]);
		}
		
		response.setCode(error);
		response.setDescription(RESPONSE_DESCRIPTIONS[error]);
		
		return new WSRspTripsVo(tripsVoList, tripUsersList, tripScheduleVehiclesList, response);
	}
	
	private List<DestinationVo>  validateOrder(List<DestinationVo> destinationVoList) {
		List<Integer> destinations = new ArrayList<Integer>();
		List<DestinationVo> destinationList = new ArrayList<DestinationVo>();
		boolean destinationAdded;
		
		for(DestinationVo destination: destinationVoList){
			if(destination.getDestinationOrder() == null || destination.getDestinationOrder() < 0){
				return new ArrayList<DestinationVo>();
			}
			destinationAdded = true;
			for(int j=0; j< destinations.size(); j++){
				if(destination.getDestinationOrder() < destinations.get(j)){
					destinationList.add(j, destination);
					destinations.add(j,destination.getDestinationOrder());
					destinationAdded = true;
					break;
				}
			}
			if(!destinationAdded){
				destinationList.add( destination);
				destinations.add(destination.getDestinationOrder());
			}
		}
		boolean validOrder = true;
		Integer prevNumber = -1;
		for(Integer number: destinations){
			if(number == 0){
				prevNumber = 0;
			}
			else if(number > 0){
				if( (number - prevNumber) != 1){
					validOrder = false;
				}
				else{
					prevNumber = number;
				}
			}
			else{
				validOrder = false;
			}
			if(!validOrder){
				return new ArrayList<DestinationVo>();
			}
		}
		return destinationList;
	}

	private List<Destination> reorderDestinationList(List<Destination> destinationDBList) throws Exception {
		List<Destination> destinationList = new ArrayList<Destination>();
		List<Integer> destinations = new ArrayList<Integer>();
		Destination destination;
		boolean destinationAdded;
		for(int i=0; i<destinationDBList.size(); i++){
			destinationAdded = false;
			destination = destinationDBList.get(i);
			for(int j=0; j< destinations.size(); j++){
				destination.toString();
				if(destination.getDestinationOrder() < destinations.get(j)){
					destinationList.add(j, destination);
					destinations.add(j,j);
					destinationAdded = true;
					break;
				}
			}
			if(!destinationAdded){
				destinationList.add( destination);
				destinations.add(destination.getDestinationOrder());
			}
		}
		return destinationList;
	}/**/
   
	public WSRspTripsVo updateTripScheduleVehicles(UserWSVo userWS, WSRqstTripVo tripWSReqVo, WebserviceType origin) {
		//System.out.println(origin.getWebserviceType() + "@TripsWS@updateTripScheduleVehicles requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString() + "@" + tripWSReqVo.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("TripsWS");
		webserviceRequest.setWebserviceFunctionRequested("updateTripScheduleVehicles");
		
		errorFlag = false;
		error = SUCCESS;
		
		WSResponseVo response = new WSResponseVo();
		List<TripVo> tripsVoList = new ArrayList<TripVo>();
		List<TripUsersVo> tripUsersList = new ArrayList<TripUsersVo>();
		List<TripScheduleVehicleVo> tripScheduleVehiclesVoList = new ArrayList<TripScheduleVehicleVo>();
		List<TripScheduleVehicleVo> tripScheduleVehicleTmpList;
		List<TripScheduleVehicles> tripScheduleVehiclesList = new ArrayList<TripScheduleVehicles>();
		
		try{
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			UserWebservices userWebservices = null;
			// UserWS Log in
			ValidUserVo validUserVo = usersMb.validateWSUser(userWS, userWebservices, "TripsWS@updateTripScheduleVehicles");
			errorFlag = validUserVo.isErrorFlag();
			error = validUserVo.getErrorCode();
			if(validUserVo.isValidUser()){
				userWebservices = validUserVo.getUserWebservices();
				if(tripWSReqVo.getTripScheduleVehiclesList() != null && !tripWSReqVo.getTripScheduleVehiclesList().isEmpty()){
					tripScheduleVehicleTmpList = tripWSReqVo.getTripScheduleVehiclesList();
					for(TripScheduleVehicleVo tripScheduleVehicleVo : tripScheduleVehicleTmpList){
						if(tripScheduleVehicleVo.getTripId() != null && tripScheduleVehicleVo.getTripId() > 0){
							Trips trip = tripsBo.getTrip(tripScheduleVehicleVo.getTripId());
							if(trip != null && trip.getUsuario().getIdUsuario() == userWebservices.getUsuario().getIdUsuario()){
								if(tripScheduleVehicleVo.getScheduleId() != null && tripScheduleVehicleVo.getScheduleId() > 0){
									Schedules schedule = schedulesBo.getSchedule(tripScheduleVehicleVo.getScheduleId());
									if(schedule != null && schedule.getUsuario().getIdUsuario() == userWebservices.getUsuario().getIdUsuario()){
										if(tripScheduleVehicleVo.getVehicleId() != null && tripScheduleVehicleVo.getVehicleId() > 0){
											Vehiculo vehicle = vehiclesBo.getVehicle(tripScheduleVehicleVo.getVehicleId());
											if(vehicle != null && vehicle.getCliente().getIdCliente() == userWebservices.getUsuario().getPerfil().getCliente().getIdCliente()){
												TripScheduleVehicles tripScheduleVehicle = tripsBo.getTripScheduleVehicle(trip.getId(), schedule.getId(), vehicle.getIdVehiculo());
												if(tripScheduleVehicle != null){
													tripScheduleVehiclesList.add(tripScheduleVehicle);
													error = EXISTENT_RELATIONS;
												}
												else{
													tripScheduleVehiclesList.add(new TripScheduleVehicleVo().generateTo(trip, schedule, vehicle));
												}
											}
											else{
												errorFlag = true;
												error = PROHIBITED_ACTION;
											}
										}
										else{
											errorFlag = true;
											error = VEHICLE_ID_EMPTY;
										}
									}
									else{
										errorFlag = true;
										error = PROHIBITED_ACTION;
									}
								}
								else{
									errorFlag = true;
									error = SCHEDULE_ID_EMPTY;
								}
							}
							else{
								errorFlag = true;
								error = PROHIBITED_ACTION;
							}
						}
						else{
							errorFlag = true;
							error = TRIP_ID_EMPTY;
						}
					}
					if(!errorFlag){
						tripScheduleVehiclesList = tripsBo.saveTripScheduleVehiclesList(tripScheduleVehiclesList);
						for(TripScheduleVehicles tripscheduleVehicle: tripScheduleVehiclesList){
							tripScheduleVehiclesVoList.add(new TripScheduleVehicleVo(tripscheduleVehicle));
						}
					}
				}
				else{
					errorFlag = true;
					error = RELATIONS_LIST_EMPTY;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			error = SERVER_ERROR;
			//System.out.println(RESPONSE_DESCRIPTIONS[error]);
		}
		
		response.setCode(error);
		response.setDescription(RESPONSE_DESCRIPTIONS[error]);

		WSRspTripsVo wsResp = new WSRspTripsVo(tripsVoList, tripUsersList, tripScheduleVehiclesVoList, response);
		webserviceRequest.setResponse(wsResp.toString());
		webserviceRequest.setResponseDate(new Date());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}
	  
	public WSRspTripsVo deleteTripScheduleVehicles(UserWSVo userWS, WSRqstTripVo tripWSReqVo, WebserviceType origin) {
		//System.out.println(origin.getWebserviceType() + "@TripsWS@deleteTripScheduleVehicles requested");
		// Decrypt userWS data: user and password
		UserWSVo userEncrypted = userWS;
		userWS = Utils.decryptUser(userEncrypted);
		// Requests logging
		WebserviceRequest webserviceRequest = new WebserviceRequest();
		webserviceRequest.setRequestDate(new Date());
		webserviceRequest.setRequest(userWS.toString() + "¬" + userEncrypted.toString() + "@" + tripWSReqVo.toString());
		webserviceRequest.setIp(userWS.getIp());
		webserviceRequest.setWebserviceType(new WebserviceType(origin.getId()));
		webserviceRequest.setWebserviceRequested("TripsWS");
		webserviceRequest.setWebserviceFunctionRequested("deleteTripScheduleVehicles");
		
		errorFlag = false;
		error = SUCCESS;

		WSResponseVo response = new WSResponseVo();
		List<TripVo> tripsVoList = new ArrayList<TripVo>();
		List<TripUsersVo> tripUsersList = new ArrayList<TripUsersVo>();
		List<TripScheduleVehicleVo> tripScheduleVehiclesVoList = new ArrayList<TripScheduleVehicleVo>();
		List<TripScheduleVehicleVo> tripScheduleVehicleTmpList;
		List<TripScheduleVehicles> tripScheduleVehiclesList = new ArrayList<TripScheduleVehicles>();
		
		try{
			//System.out.println(userWS.getUser() + " :: " + userWS.getPassword());
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			UserWebservices userWebservices = null;
			// UserWS Log in
			ValidUserVo validUserVo = usersMb.validateWSUser(userWS, userWebservices, "TripsWS@deleteTripScheduleVehicles");
			errorFlag = validUserVo.isErrorFlag();
			error = validUserVo.getErrorCode();
			if(validUserVo.isValidUser()){
				userWebservices = validUserVo.getUserWebservices();
				if(tripWSReqVo.getTripScheduleVehiclesList() != null && !tripWSReqVo.getTripScheduleVehiclesList().isEmpty()){
					tripScheduleVehicleTmpList = tripWSReqVo.getTripScheduleVehiclesList();
					for(TripScheduleVehicleVo tripScheduleVehicleVo : tripScheduleVehicleTmpList){

						if(tripScheduleVehicleVo.getTripId() != null && tripScheduleVehicleVo.getTripId() > 0){
							Trips trip = tripsBo.getTrip(tripScheduleVehicleVo.getTripId());
							if(trip != null && trip.getUsuario().getIdUsuario() == userWebservices.getUsuario().getIdUsuario()){
								if(tripScheduleVehicleVo.getScheduleId() != null && tripScheduleVehicleVo.getScheduleId() > 0){
									Schedules schedule = schedulesBo.getSchedule(tripScheduleVehicleVo.getScheduleId());
									if(schedule != null && schedule.getUsuario().getIdUsuario() == userWebservices.getUsuario().getIdUsuario()){
										if(tripScheduleVehicleVo.getVehicleId() != null && tripScheduleVehicleVo.getVehicleId() > 0){
											Vehiculo vehicle = vehiclesBo.getVehicle(tripScheduleVehicleVo.getVehicleId());
											if(vehicle != null && vehicle.getCliente().getIdCliente() == userWebservices.getUsuario().getPerfil().getCliente().getIdCliente()){
												TripScheduleVehicles tripScheduleVehicle = tripsBo.getTripScheduleVehicle(trip.getId(), schedule.getId(), vehicle.getIdVehiculo());
												if(tripScheduleVehicle != null){
													tripScheduleVehiclesList.add(tripScheduleVehicle);
													tripsBo.deleteTripScheduleVehicle(tripScheduleVehicle);
												}
												else{
													//tripScheduleVehiclesList.add(new TripScheduleVehicleVo().generateTo(trip, schedule, vehicle));
													error = INEXISTENT_RELATIONS;
												}
											}
											else{
												errorFlag = true;
												error = PROHIBITED_ACTION;
											}
										}
										else{
											errorFlag = true;
											error = VEHICLE_ID_EMPTY;
										}
									}
									else{
										errorFlag = true;
										error = PROHIBITED_ACTION;
									}
								}
								else{
									errorFlag = true;
									error = SCHEDULE_ID_EMPTY;
								}
							}
							else{
								errorFlag = true;
								error = PROHIBITED_ACTION;
							}
						}
						else{
							errorFlag = true;
							error = TRIP_ID_EMPTY;
						}
					}
					if(!errorFlag){
						tripScheduleVehiclesList = tripsBo.saveTripScheduleVehiclesList(tripScheduleVehiclesList);
						for(TripScheduleVehicles tripscheduleVehicle: tripScheduleVehiclesList){
							tripScheduleVehiclesVoList.add(new TripScheduleVehicleVo(tripscheduleVehicle));
						}
					}
				}
				else{
					errorFlag = true;
					error = RELATIONS_LIST_EMPTY;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			error = SERVER_ERROR;
			//System.out.println(RESPONSE_DESCRIPTIONS[error]);
		}
		
		response.setCode(error);
		response.setDescription(RESPONSE_DESCRIPTIONS[error]);

		WSRspTripsVo wsResp = new WSRspTripsVo(tripsVoList, tripUsersList, tripScheduleVehiclesVoList, response);
		webserviceRequest.setResponse(wsResp.toString());
		webserviceRequest.setResponseDate(new Date());
		if(ConstantsWS.ENABLE_LOGGING){
			webservicesBo.saveWebserviceRequest(webserviceRequest);
		}
		return wsResp;
	}
}
