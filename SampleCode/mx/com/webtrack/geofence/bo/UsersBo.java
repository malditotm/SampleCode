package mx.com.webtrack.qbo.webservices.bo;

import java.util.ArrayList;
import java.util.List;

import mx.com.webtrack.qbo.dao.GenericDao;
import mx.com.webtrack.qbo.to.UserWebservices;
import mx.com.webtrack.qbo.to.Usuario;
import mx.com.webtrack.qbo.webservices.vo.UserVo;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("usersBo")
public class UsersBo {
	@Autowired
	GenericDao genericDao;
	
	public UserWebservices logUserWebservices(String user){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserWebservices.class);
		detachedCriteria.add(Restrictions.eq("userName", user));
		detachedCriteria.setFetchMode("usuario", FetchMode.JOIN);
		detachedCriteria.setFetchMode("usuario.perfil", FetchMode.JOIN);
		detachedCriteria.setFetchMode("usuario.perfil.cliente", FetchMode.JOIN); 
		detachedCriteria.setFetchMode("userStatus", FetchMode.JOIN);
		detachedCriteria.createCriteria("userStatus").add(Restrictions.eq("id", 1));
		return (UserWebservices) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}

	public Usuario validateUser(Integer userId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Usuario.class);
		detachedCriteria.add(Restrictions.eq("idUsuario", userId));
		detachedCriteria.setFetchMode("perfil", FetchMode.JOIN);
		detachedCriteria.setFetchMode("perfil.cliente", FetchMode.JOIN);
		detachedCriteria.setFetchMode("perfil.cliente.cliente", FetchMode.JOIN);		
		//detachedCriteria.setFetchMode("perfil.cliente.tipoCliente", FetchMode.JOIN);
		return (Usuario) genericDao.findByCriteriaUniqueResult(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserVo> getClientUsers(Integer idClient){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Usuario.class);
		detachedCriteria.createCriteria("perfil").createAlias("cliente", "cl").createAlias("tipoPerfil", "pe").add(Restrictions.eq("cl.idCliente", idClient)).add(Restrictions.or(Restrictions.eq("pe.idTipoPerfil", 3),Restrictions.eq("pe.idTipoPerfil", 2)));
		List<Usuario> usuariosList = (List<Usuario>) genericDao.findByCriteria(detachedCriteria);
		List<UserVo> usersList = new ArrayList<UserVo>();
		for(Usuario usuario : usuariosList){
			UserVo user = new UserVo();
			user.setUserId(usuario.getIdUsuario());
			user.setUser(usuario.getUsuario());
			usersList.add(user);
		}
		return usersList;
	}
}
