/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class InfoadicionalDao extends AppGenericDao<Infoadicional, String> {

	/**
	 * @param type
	 */
	public InfoadicionalDao() {
		super(Infoadicional.class);
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
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM infoadicional WHERE idcabecera=:id");
			q.setParameter("id", idCabecera);
			
			return q.executeUpdate();
			
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
	@SuppressWarnings("unchecked")
	public List<Infoadicional> getByIdCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Infoadicional d WHERE d.cabecera.idcabecera=:idCabecera");
			q.setParameter("idCabecera", idCabecera);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
