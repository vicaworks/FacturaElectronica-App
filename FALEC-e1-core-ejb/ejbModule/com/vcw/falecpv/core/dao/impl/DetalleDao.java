/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalleDao extends AppGenericDao<Detalle, String> {

	/**
	 * @param type
	 */
	public DetalleDao() {
		super(Detalle.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param idEstablecimeinto
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM detalle WHERE idcabecera=:id");
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
	public List<Detalle> getByIdCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT d FROM Detalle d WHERE d.cabecera.idcabecera=:idCabecera ORDER BY d.descripcion");
			q.setParameter("idCabecera", idCabecera);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
