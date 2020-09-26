/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Pago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class PagoDao extends AppGenericDao<Pago, String> {

	/**
	 * @param type
	 */
	public PagoDao() {
		super(Pago.class);
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
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM pago WHERE idcabecera=:id");
			q.setParameter("id", idCabecera);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idAdquisicion
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByAdquisicion(String idAdquisicion)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM pago WHERE idadquisicion=:id");
			q.setParameter("id", idAdquisicion);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Pago> getByIdCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT p FROM Pago p WHERE p.cabecera.idcabecera=:idCabecera ORDER BY p.idpago");
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
	public List<Pago> getByIdCabecera(List<String> idCabeceraList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT p FROM Pago p WHERE p.cabecera.idcabecera in :idCabecera ORDER BY p.idpago");
			q.setParameter("idCabecera", idCabeceraList);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * @author cristianvillarreal
	 * @param idAdquisicion
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Pago> getByIdAdquisicion(String idAdquisicion)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT p FROM Pago p WHERE p.adquisicion.idadquisicion=:idCabecera ORDER BY p.idpago");
			q.setParameter("idCabecera", idAdquisicion);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * @param idAdquisicionList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Pago> getByIdAdquisicion(List<String> idAdquisicionList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT p FROM Pago p WHERE p.adquisicion.idadquisicion in :lista ORDER BY p.adquisicion.idadquisicion,p.idpago");
			q.setParameter("lista", idAdquisicionList);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
