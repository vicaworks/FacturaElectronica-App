/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;

/**
 * @author Jorge Toaza
 *
 */
@Stateless
public class TipoIdentificacionDao extends AppGenericDao<TipoIdentificacion, String> {

	/**
	 * @param type
	 */
	public TipoIdentificacionDao() {
		super(TipoIdentificacion.class);
	}

	@SuppressWarnings("unchecked")
	public List<TipoIdentificacion> consultarTiposIdentificacion() throws DaoException {
		try {
			
			String sql = "SELECT c FROM TipoIdentificacion c ";
			sql += "ORDER BY c.codigo";
			
			Query q = getEntityManager().createQuery(sql);	
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}