/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class UsuarioDao extends AppGenericDao<Usuario, String> {

	/**
	 * @param type
	 */
	public UsuarioDao() {
		super(Usuario.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param login
	 * @return
	 * @throws DaoException
	 */
	public Usuario getByLogin(String login)throws DaoException{
		
		Query q = getEntityManager().createQuery("SELECT u FROM Usuario u WHERE u.login=:login");
		q.setParameter("login", login);
		
		try {
			
			return (Usuario) q.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> getByEstado(EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		try {
			Query q = null;
			if(estadoRegistroEnum!=null) {
				q = getEntityManager().createQuery("SELECT u FROM Usuario u WHERE u.estado=:estado ORDER BY u.nombre");
				q.setParameter("estado", estadoRegistroEnum.getInicial());
			}else {
				q = getEntityManager().createQuery("SELECT u FROM Usuario u ORDER BY u.nombre");
			}
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
