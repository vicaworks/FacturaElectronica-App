/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Errorsri;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ErrorsriDao extends AppGenericDao<Errorsri, String> {

	/**
	 * @param type
	 */
	public ErrorsriDao() {
		super(Errorsri.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param identificador
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public Errorsri getByCodErrorSri(String identificador)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT e FROM Errorsri e WHERE e.coderror=:coderror");
			q.setParameter("coderror", identificador);
			
			List<Errorsri> r = q.getResultList();
			if(!r.isEmpty()) {
				return r.get(0);
			}
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
