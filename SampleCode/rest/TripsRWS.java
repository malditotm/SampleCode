package mx.com.webtrack.qbo.webservices.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import mx.com.webtrack.qbo.to.WebserviceType;
import mx.com.webtrack.qbo.webservices.mb.TripsMb;
import mx.com.webtrack.qbo.webservices.rest.vo.WSRqstTripsXML;
import mx.com.webtrack.qbo.webservices.rest.vo.WSRspTripsXML;
import mx.com.webtrack.qbo.webservices.utils.ConstantsWS;
import mx.com.webtrack.qbo.webservices.vo.WSResponseVo;
import mx.com.webtrack.qbo.webservices.vo.WSRspTripsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

// Add this class to RESTWS singletons
@Path("/TripsRWS")
public class TripsRWS {
	@Autowired
	TripsMb tripsMb;
	private static WebserviceType ORIGIN = new WebserviceType(ConstantsWS.WEBSERVICE_REST_ID, ConstantsWS.WEBSERVICE_REST_NAME);
	
	public TripsRWS() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Path("/getAllTrips")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public WSRspTripsXML getAllTrips(@Context HttpServletRequest requestContext, @Context SecurityContext context,
					WSRqstTripsXML rqstTrips){
		String yourIP = requestContext.getRemoteAddr().toString();
		//System.out.println("Excecuting POST");
    	if(rqstTrips.getUserWS() != null){
    		//System.out.println(rqstTrips.getUserWS().toString());
    		rqstTrips.getUserWS().setIp(yourIP);
    		//System.out.println(rqstTrips.getUserWS().toString());
    		WSRspTripsXML vehiclesList = new WSRspTripsXML(tripsMb.getAllTrips(rqstTrips.getUserWS(), ORIGIN));
		    				
			return vehiclesList;
    	}
		return new WSRspTripsXML(noUserProvided());
	}
	
	@Path("/getTripByID")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public WSRspTripsXML getTripByID(@Context HttpServletRequest requestContext, @Context SecurityContext context,
					WSRqstTripsXML rqstTrips){
		String yourIP = requestContext.getRemoteAddr().toString();
		//System.out.println("Excecuting POST");
    	if(rqstTrips.getUserWS() != null){
    		//System.out.println(rqstTrips.getUserWS().toString());
    		rqstTrips.getUserWS().setIp(yourIP);
    		//System.out.println(rqstTrips.getUserWS().toString());
    		WSRspTripsXML vehiclesList = new WSRspTripsXML(tripsMb.getTripByID(rqstTrips.getUserWS(), rqstTrips, ORIGIN));
		    				
			return vehiclesList;
    	}
		return new WSRspTripsXML(noUserProvided());
	}
	
	@Path("/createTrip")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public WSRspTripsXML createTrip(@Context HttpServletRequest requestContext, @Context SecurityContext context,
					WSRqstTripsXML rqstTrips){
		String yourIP = requestContext.getRemoteAddr().toString();
		//System.out.println("Excecuting POST");
    	if(rqstTrips.getUserWS() != null){
    		//System.out.println(rqstTrips.getUserWS().toString());
    		rqstTrips.getUserWS().setIp(yourIP);
    		//System.out.println(rqstTrips.getUserWS().toString());
    		WSRspTripsXML vehiclesList = new WSRspTripsXML(tripsMb.createTrip(rqstTrips.getUserWS(), rqstTrips, ORIGIN));
		    				
			return vehiclesList;
    	}
		return new WSRspTripsXML(noUserProvided());
	}
	
	@Path("/updateTrip")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public WSRspTripsXML updateTrip(@Context HttpServletRequest requestContext, @Context SecurityContext context,
					WSRqstTripsXML rqstTrips){
		String yourIP = requestContext.getRemoteAddr().toString();
		//System.out.println("Excecuting POST");
    	if(rqstTrips.getUserWS() != null){
    		//System.out.println(rqstTrips.getUserWS().toString());
    		rqstTrips.getUserWS().setIp(yourIP);
    		//System.out.println(rqstTrips.getUserWS().toString());
    		WSRspTripsXML vehiclesList = new WSRspTripsXML(tripsMb.updateTrip(rqstTrips.getUserWS(), rqstTrips, ORIGIN));
		    				
			return vehiclesList;
    	}
		return new WSRspTripsXML(noUserProvided());
	}
	
	@Path("/deleteTrip")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public WSRspTripsXML deleteTrip(@Context HttpServletRequest requestContext, @Context SecurityContext context,
					WSRqstTripsXML rqstTrips){
		String yourIP = requestContext.getRemoteAddr().toString();
		//System.out.println("Excecuting POST");
    	if(rqstTrips.getUserWS() != null){
    		//System.out.println(rqstTrips.getUserWS().toString());
    		rqstTrips.getUserWS().setIp(yourIP);
    		//System.out.println(rqstTrips.getUserWS().toString());
    		WSRspTripsXML vehiclesList = new WSRspTripsXML(tripsMb.deleteTrip(rqstTrips.getUserWS(), rqstTrips, ORIGIN));
		    				
			return vehiclesList;
    	}
		return new WSRspTripsXML(noUserProvided());
	}
	
	@Path("/updateTripScheduleVehicles")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public WSRspTripsXML updateTripScheduleVehicles(@Context HttpServletRequest requestContext, @Context SecurityContext context,
					WSRqstTripsXML rqstTrips){
		String yourIP = requestContext.getRemoteAddr().toString();
		//System.out.println("Excecuting POST");
    	if(rqstTrips.getUserWS() != null){
    		//System.out.println(rqstTrips.getUserWS().toString());
    		rqstTrips.getUserWS().setIp(yourIP);
    		//System.out.println(rqstTrips.getUserWS().toString());
    		WSRspTripsXML vehiclesList = new WSRspTripsXML(tripsMb.updateTripScheduleVehicles(rqstTrips.getUserWS(), rqstTrips, ORIGIN));
		    				
			return vehiclesList;
    	}
		return new WSRspTripsXML(noUserProvided());
	}
	
	@Path("/deleteTripScheduleVehicles")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public WSRspTripsXML deleteTripScheduleVehicles(@Context HttpServletRequest requestContext, @Context SecurityContext context,
					WSRqstTripsXML rqstTrips){
		String yourIP = requestContext.getRemoteAddr().toString();
		//System.out.println("Excecuting POST");
    	if(rqstTrips.getUserWS() != null){
    		//System.out.println(rqstTrips.getUserWS().toString());
    		rqstTrips.getUserWS().setIp(yourIP);
    		//System.out.println(rqstTrips.getUserWS().toString());
    		WSRspTripsXML vehiclesList = new WSRspTripsXML(tripsMb.deleteTripScheduleVehicles(rqstTrips.getUserWS(), rqstTrips, ORIGIN));
		    				
			return vehiclesList;
    	}
		return new WSRspTripsXML(noUserProvided());
	}
	
	public WSRspTripsVo noUserProvided(){
		WSResponseVo response = new WSResponseVo();
		response.setCode(4);
		response.setDescription("No userWS provided");
		return new WSRspTripsVo(null, null, null, response);
	}
}
