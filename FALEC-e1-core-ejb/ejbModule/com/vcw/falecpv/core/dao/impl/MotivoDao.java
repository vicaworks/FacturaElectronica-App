/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class MotivoDao extends AppGenericDao<Motivo, String> {

	/**
	 * @param type
	 */
	public MotivoDao() {
		super(Motivo.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Motivo> getByIdCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Motivo d WHERE d.cabecera.idcabecera=:idCabecera ORDER BY d.razon");
			q.setParameter("idCabecera", idCabecera);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabeceraList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Motivo> getByIdCabecera(List<String> idCabeceraList)throws DaoException{
		try {
			Query q = getEntityManager().createQuery("SELECT d FROM Motivo d WHERE d.cabecera.idcabecera in :idCabecera ORDER BY d.razon");
			q.setParameter("idCabecera", idCabeceraList);
			
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM motivo WHERE idcabecera=:idcabecera");
			q.setParameter("idcabecera", idCabecera);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
