package mx.com.webtrack.qbo.webservices.bo;

import java.util.ArrayList;
import java.util.List;

import mx.com.webtrack.qbo.dao.GenericDao;
import mx.com.webtrack.qbo.to.Destination;
import mx.com.webtrack.qbo.to.GeofencesRoute;
import mx.com.webtrack.qbo.to.TripDestination;
import mx.com.webtrack.qbo.to.TripScheduleVehicles;
import mx.com.webtrack.qbo.to.Trips;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("tripsBo")
public class TripsBo {
	@Autowired
	GenericDao genericDao;
	
	@SuppressWarnings("unchecked")
	public List<Trips> getTrips(Integer userId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Trips.class);
		detachedCriteria.createCriteria("usuario").add(Restrictions.eq("idUsuario", userId));
		detachedCriteria.createCriteria("tripStatus").add(Restrictions.not(Restrictions.eq("id", 3)));
		detachedCriteria.setFetchMode("tripDestinations", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination.geofencesRoute", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripUsers", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripUsers.usuario", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.trips", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.schedules", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.vehiculo", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripStatus", FetchMode.JOIN);
		return (List<Trips>) genericDao.findByCriteria(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<Trips> getTripByID(Integer userId, Integer tripId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Trips.class);
		detachedCriteria.add(Restrictions.eq("id", tripId));
		detachedCriteria.createCriteria("usuario").add(Restrictions.eq("idUsuario", userId));
		detachedCriteria.createCriteria("tripStatus").add(Restrictions.not(Restrictions.eq("id", 3)));
		detachedCriteria.setFetchMode("tripDestinations", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination.geofencesRoute", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripUsers", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripUsers.usuario", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.trips", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.schedules", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.vehiculo", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripStatus", FetchMode.JOIN);
		return (List<Trips>) genericDao.findByCriteria(detachedCriteria);
	}
	
	public Trips getTrip(Integer tripId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Trips.class);
		detachedCriteria.add(Restrictions.eq("id", tripId));
		detachedCriteria.createCriteria("tripStatus").add(Restrictions.not(Restrictions.eq("id", 3)));
		detachedCriteria.setFetchMode("usuario", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripStatus", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.trips", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination.geofencesRoute", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination.geofencesRoute.geofencesStatus", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination.geofencesRoute.geofencesType", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.destination.markerType", FetchMode.JOIN);/**/
		//detachedCriteria.addOrder( Property.forName("tripDestinations.destination.id").asc() );
		return (Trips) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}
	public Trips getTrip(String name, Integer userId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Trips.class);
		detachedCriteria.createCriteria("usuario").add(Restrictions.eq("idUsuario", userId));
		detachedCriteria.add(Restrictions.eq("name", name));
		detachedCriteria.setFetchMode("usuario", FetchMode.JOIN);
		return (Trips) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<TripDestination> getTripDestination(Trips trip){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TripDestination.class);
		detachedCriteria.setFetchMode("trips", FetchMode.JOIN);
		detachedCriteria.setFetchMode("trips.tripStatus", FetchMode.JOIN);
		detachedCriteria.setFetchMode("destination", FetchMode.JOIN);
		detachedCriteria.setFetchMode("destination.geofencesRoute", FetchMode.JOIN);
		detachedCriteria.createCriteria("trips").add(Restrictions.eq("id", trip.getId()));
		
		return (List<TripDestination>) genericDao.findByCriteria(detachedCriteria);
	}
	
	public void saveOrUpdate(Trips trip) {
		genericDao.saveOrUpdate(trip);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveOrUpdate(Trips trip, List<Destination> destinationList, List<GeofencesRoute> deletingGeofenceRouteList){
		List<TripDestination> tripDestinationList = new ArrayList<TripDestination>();
		trip = (Trips) genericDao.saveOrUpdateReturn(trip);
		
		DetachedCriteria detachedCriteria =  DetachedCriteria.forClass(TripDestination.class);
		detachedCriteria.setFetchMode("trips", FetchMode.JOIN);
		detachedCriteria.createCriteria("trips").add(Restrictions.eq("id", trip.getId()));
		List<TripDestination> tripDestinationsList = (List<TripDestination>) genericDao.findByCriteria(detachedCriteria);
		genericDao.deleteAll(tripDestinationsList);
		
		if(deletingGeofenceRouteList != null && !deletingGeofenceRouteList.isEmpty()){
			for(GeofencesRoute geofence: deletingGeofenceRouteList){
				geofence.getGeofencesStatus().setId(3);
			}
			genericDao.saveOrUpdateAll(deletingGeofenceRouteList);
		}
		
		for(Destination destination: destinationList){
			destination.setGeofencesRoute((GeofencesRoute) genericDao.saveOrUpdateReturn(destination.getGeofencesRoute()));
			destination = (Destination) genericDao.saveOrUpdateReturn(destination);
			tripDestinationList.add(new TripDestination(trip, destination));
		}
		genericDao.saveOrUpdateAll(tripDestinationList);
		return true;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<TripScheduleVehicles> saveTripScheduleVehiclesList(List<TripScheduleVehicles> tripScheduleVehiclesList) {
		genericDao.saveOrUpdateAll(tripScheduleVehiclesList);
		return tripScheduleVehiclesList;
	}

	public TripScheduleVehicles getTripScheduleVehicle(Integer tripId, Integer scheduleId, Integer vehicleId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TripScheduleVehicles.class);
		detachedCriteria.setFetchMode("trips", FetchMode.JOIN);
		detachedCriteria.createCriteria("trips").add(Restrictions.eq("id", tripId));
		detachedCriteria.setFetchMode("schedules", FetchMode.JOIN);
		detachedCriteria.createCriteria("schedules").add(Restrictions.eq("id", scheduleId));
		detachedCriteria.setFetchMode("vehiculo", FetchMode.JOIN);
		detachedCriteria.createCriteria("vehiculo").add(Restrictions.eq("id", vehicleId));
		return (TripScheduleVehicles) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}

	public void deleteTripScheduleVehicle(TripScheduleVehicles tripScheduleVehicle){
		genericDao.delete(tripScheduleVehicle);
	}

	@SuppressWarnings("unchecked")
	public List<Destination> getDestinations(Integer idTrip) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Destination.class);
		detachedCriteria.createCriteria("tripDestinations.trips").add(Restrictions.eqOrIsNull("id", idTrip));
		detachedCriteria.setFetchMode("destination", FetchMode.JOIN);
		detachedCriteria.setFetchMode("destination.geofencesRoute", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripDestinations.trips", FetchMode.JOIN);
		return (List<Destination>) genericDao.findByCriteria(detachedCriteria);
	}
}
