package mx.com.webtrack.qbo.webservices.bo;

import java.util.List;

import mx.com.webtrack.qbo.dao.GenericDao;
import mx.com.webtrack.qbo.to.Schedules;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("schedulesBo")
public class SchedulesBo {
	@Autowired
	GenericDao genericDao;

	@SuppressWarnings("unchecked")
	public List<Schedules> getSchedules(Integer userId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Schedules.class);
		detachedCriteria.createCriteria("usuario").add(Restrictions.eqOrIsNull("idUsuario", userId));
		detachedCriteria.createCriteria("scheduleStatus").add(Restrictions.not(Restrictions.eq("id", 3)));
		detachedCriteria.setFetchMode("frecuencyTypes", FetchMode.JOIN);
		detachedCriteria.setFetchMode("scheduleStatus", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.trips", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.schedules", FetchMode.JOIN);
		detachedCriteria.setFetchMode("tripScheduleVehicles.vehiculo", FetchMode.JOIN);
		
		return (List<Schedules>) genericDao.findByCriteria(detachedCriteria);
	}
	
	public Schedules getScheduleByID(Integer userId, Integer scheduleId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Schedules.class);
		detachedCriteria.add(Restrictions.eq("id", scheduleId));
		detachedCriteria.createCriteria("usuario").add(Restrictions.eqOrIsNull("idUsuario", userId));
		detachedCriteria.createCriteria("scheduleStatus").add(Restrictions.not(Restrictions.eq("id", 3)));
		detachedCriteria.setFetchMode("usuario", FetchMode.JOIN);
		return (Schedules) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}
	
	public Schedules getSchedule(Integer scheduleId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Schedules.class);
		detachedCriteria.add(Restrictions.eq("id", scheduleId));
		detachedCriteria.createCriteria("scheduleStatus").add(Restrictions.not(Restrictions.eq("id", 3)));
		detachedCriteria.setFetchMode("usuario", FetchMode.JOIN);
		return (Schedules) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}

	public void saveOrUpdate(Schedules schedule) {
		genericDao.saveOrUpdate(schedule);
	}
}
