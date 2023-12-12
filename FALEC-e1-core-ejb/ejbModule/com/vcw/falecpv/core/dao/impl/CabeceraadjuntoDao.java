/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabeceraadjunto;

/**
 * 
 */
@Stateless
public class CabeceraadjuntoDao extends AppGenericDao<Cabeceraadjunto, String> {

	/**
	 * @param type
	 */
	public CabeceraadjuntoDao() {
		super(Cabeceraadjunto.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cabeceraadjunto> getByCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM Cabeceraadjunto c WHERE c.cabecera.idcabecera=:id ORDER BY c.nombreadjunto");
			q.setParameter("id", idCabecera);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM Cabeceraadjunto WHERE idcabecera=:id");
			q.setParameter("id", idCabecera);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
