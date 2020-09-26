/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisiciondetalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class AdquisiciondetalleDao extends AppGenericDao<Adquisiciondetalle, String> {

	/**
	 * @param type
	 */
	public AdquisiciondetalleDao() {
		super(Adquisiciondetalle.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Adquisiciondetalle> getByIdAdquisicion(String id)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Adquisiciondetalle d WHERE d.adquisicion.idadquisicion=:id ORDER BY d.descripcion");
			q.setParameter("id", id);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Adquisiciondetalle> getByIdAdquisicion(List<String> idList)throws DaoException{
		try {
			Query q = getEntityManager().createQuery("SELECT d FROM Adquisiciondetalle d WHERE d.adquisicion.idadquisicion in :lista ORDER BY d.descripcion");
			q.setParameter("lista", idList);
			
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
