package mx.com.webtrack.qbo.webservices.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import mx.com.webtrack.qbo.dao.GenericDao;
import mx.com.webtrack.qbo.to.Vehiculo;
import mx.com.webtrack.qbo.webservices.vo.VehicleVo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("vehiclesBo")
public class VehiclesBo {
	@Autowired
	GenericDao genericDao;
	
	public List<VehicleVo> getUserVehicles(Integer userId) {
		List<VehicleVo> vehicles = new ArrayList<VehicleVo>();
		StringBuffer query = new StringBuffer();
		query.append(" select v.idVehiculo,v.nombre,v.localizador,m.nombre as brand_name,mdl.nombre as model_name,v.velocidadMaxima  ");
		query.append(" from UsuarioVehiculo uv ");
		query.append(" join Vehiculo v on uv.idVehiculo=v.idVehiculo ");
		query.append(" left join marca m on m.idMarca=v.idMarca ");
		query.append(" left join modelo mdl on mdl.idModelo=v.idModelo ");
		query.append(" where uv.idUsuario= ");
		query.append(userId);
		query.append(" and v.idVehiculo is not null ");

		@SuppressWarnings("unchecked")
		List<Object[]> resp = (List<Object[]>) genericDao.executeNativeSqlQuery(query.toString());

		for (Object[] aux : resp) {
			VehicleVo vehicle = new VehicleVo();
			vehicle.setId((Integer) aux[0]);
			vehicle.setVehicleName(((String) aux[1]).trim());
			vehicle.setLocatorNumber((String) aux[2]);
			vehicle.setVehicleBrand((String) aux[3]);
			vehicle.setVehicleModel((String) aux[4]);
			vehicle.setMaxSpeed(((BigDecimal) aux[5]).floatValue());
			vehicles.add(vehicle);
		}
		return vehicles;
	}
	
	public Vehiculo getVehicle(Integer vehicleId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Vehiculo.class);
		detachedCriteria.add(Restrictions.eq("idVehiculo", vehicleId));
		return (Vehiculo) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}

}
