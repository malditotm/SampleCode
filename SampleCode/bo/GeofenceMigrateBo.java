package mx.com.webtrack.qbo.geofence.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.webtrack.qbo.dao.GenericDao;
import mx.com.webtrack.qbo.geofence.vo.GeofenceMigrateUsersVo;
import mx.com.webtrack.qbo.to.GeofenceUser;
import mx.com.webtrack.qbo.to.Geofences;
import mx.com.webtrack.qbo.to.GeofencesVehicle;
import mx.com.webtrack.qbo.to.Usuario;

import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("geofenceMigrateBo")
public class GeofenceMigrateBo implements Serializable{
	private static final long serialVersionUID = -825360356968871684L;

	@Autowired
	private GenericDao genericDao;
	
	@SuppressWarnings("unchecked")
	public List<GeofencesVehicle> getGeofenceVehicules(Integer idGeofence){
		List<GeofencesVehicle> allGeofencesVehicle = new ArrayList<GeofencesVehicle>();
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GeofencesVehicle.class);		
		detachedCriteria.createCriteria("geofences").add(Restrictions.like("id",idGeofence));		
		allGeofencesVehicle = (List<GeofencesVehicle>)genericDao.findByCriteria(detachedCriteria);
		return allGeofencesVehicle;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> searchUsersByClientId(Integer idClient) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Usuario.class);
		detachedCriteria.createCriteria("perfil").createAlias("cliente", "cl").createAlias("tipoPerfil", "pe").add(Restrictions.eq("cl.idCliente", idClient)).add(Restrictions.or(Restrictions.eq("pe.idTipoPerfil", 3),Restrictions.eq("pe.idTipoPerfil", 2)));
		detachedCriteria.addOrder(Order.asc("nombre")).addOrder(Order.asc("apellidoPaterno"));
		return (List<Usuario>) genericDao.findByCriteria(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<GeofenceMigrateUsersVo> getUsers(Integer idUsuario, Integer idGeofences, String name, Integer idCliente){
		StringBuffer query = new StringBuffer();
		query.append("        select distinct u.idUsuario id,");
		query.append("        u.nombre nombreUsuario,");
		query.append("        u.apellidoPaterno paternoUsuario,");
		query.append("        u.apellidoMaterno maternoUsuario");
		query.append("        FROM usuario u ");
		query.append(" inner join perfil p on p.idperfil = u.idperfil ");
		query.append(" where idUsuario in(");
		query.append("        select idUsuario FROM usuarioVehiculo where idVehiculo in(");
		query.append("        select id_Vehicle FROM geofences_vehicle");
		query.append("        where id_geofences=" + idGeofences+ ")");
		query.append(" )");
		query.append(" and p.idcliente = " + idCliente);
		return procesaUsuarios((List<Object[]>) genericDao.executeNativeSqlQuery(query.toString()));
	}	
	
	private List<GeofenceMigrateUsersVo> procesaUsuarios(List<Object[]> listaObjetos){
		GeofenceMigrateUsersVo geofenceMigrateUser;
		Usuario user;
		List<GeofenceMigrateUsersVo> listaUsuarios = new ArrayList<GeofenceMigrateUsersVo>();
		if(listaObjetos!=null){
			Iterator<Object[]> ite = listaObjetos.iterator();
			while (ite.hasNext()) {
				Object[] objeto = ite.next();
				user = new Usuario();
				user.setIdUsuario(objeto[0] == null ? 0 : Integer.parseInt(String.valueOf(objeto[0])));
	 			user.setNombre(objeto[1] == null ? "" : String.valueOf(objeto[1]));
				user.setApellidoPaterno(objeto[2] == null ? "" : String.valueOf(objeto[2]));
				user.setApellidoPaterno(objeto[3] == null ? "" : String.valueOf(objeto[3]));
				geofenceMigrateUser = new GeofenceMigrateUsersVo(user);
				listaUsuarios.add(geofenceMigrateUser);
			}
		}
		return listaUsuarios;
	}
	
	public Usuario getUser(Integer userId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Geofences.class);		
		detachedCriteria.add(Restrictions.eq("idUsuario", userId));
		return (Usuario)genericDao.findByCriteria(detachedCriteria);
	}	
	
	public Geofences getGeofence(Integer geofenceId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Geofences.class);		
		detachedCriteria.add(Restrictions.eq("id", geofenceId));
		return (Geofences)genericDao.findByCriteria(detachedCriteria);
	}	
	
	@SuppressWarnings("unchecked")
	public List<Geofences> getGeofencesByUser(Usuario user){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Geofences.class);
		detachedCriteria.createAlias("geofencesGeofenceUsers.usuario", "usuario");
		detachedCriteria.add(Restrictions.or(Restrictions.eq("user.idUsuario", user.getIdUsuario()),
						Restrictions.eq("usuario.idUsuario", user.getIdUsuario())));
		detachedCriteria.setFetchMode("geofencesStatus", FetchMode.JOIN);		
		detachedCriteria.setFetchMode("geofencesNotificationType", FetchMode.JOIN);
		detachedCriteria.setFetchMode("geofencesGeofenceUsers", FetchMode.JOIN);
		detachedCriteria.setFetchMode("geofencesGeofenceUsers.usuario", FetchMode.JOIN);		
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);				
		detachedCriteria.setFetchMode("geofencesType", FetchMode.JOIN);
		return (List<Geofences>)genericDao.findByCriteria(detachedCriteria);
	}	

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Geofences saveGeofence(Geofences geofence){
		return (Geofences)genericDao.saveOrUpdateReturn(geofence);
	}
	
	@Transactional(propagation=Propagation.REQUIRED ,rollbackFor=Exception.class)
	public GeofencesVehicle saveGeofencesVehicle(GeofencesVehicle geofencesVehicle){
		return (GeofencesVehicle)genericDao.saveOrUpdateReturn(geofencesVehicle);
	}
	
	@Transactional(propagation=Propagation.REQUIRED ,rollbackFor=Exception.class)
	public void deleteGeofencesVehicle(GeofencesVehicle geofencesVehicle){
		genericDao.delete(geofencesVehicle);
	}
	
	@SuppressWarnings("unchecked")
	public boolean existeGeocercaUsuario(String name, Integer idUsuario){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Geofences.class);
		detachedCriteria.createAlias("geofencesGeofenceUsers.usuario", "usuario");
		detachedCriteria.add(Restrictions.eq("usuario.idUsuario", idUsuario));
		detachedCriteria.add(Restrictions.eq("name",name));
		detachedCriteria.setFetchMode("geofencesGeofenceUsers", FetchMode.JOIN);
		detachedCriteria.setFetchMode("geofencesGeofenceUsers.usuario", FetchMode.JOIN);
		List<Geofences> item = (List<Geofences>)genericDao.findByCriteria(detachedCriteria);
		if(item == null || item.isEmpty()){
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED ,rollbackFor=Exception.class)
	public void saveGeofenceUser(Map<Integer, List<GeofenceUser>> geofencesToUpdateMap) {
		List<GeofenceUser> prevGeofenceUserList;
		for(Integer geofenceId: geofencesToUpdateMap.keySet()){
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GeofenceUser.class);
			detachedCriteria.add(Restrictions.eq("geofences.id", geofenceId));
			prevGeofenceUserList = (List<GeofenceUser>) genericDao.findByCriteria(detachedCriteria);
			genericDao.deleteAll(prevGeofenceUserList);
			genericDao.saveOrUpdateAll(geofencesToUpdateMap.get(geofenceId));
		}
	}

	@SuppressWarnings("unchecked")
	public List<GeofenceUser> getGeofenceUser(Integer geofenceId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GeofenceUser.class);
		detachedCriteria.add(Restrictions.eq("geofences.id", geofenceId));
		return (List<GeofenceUser>) genericDao.findByCriteria(detachedCriteria);
	}
}
