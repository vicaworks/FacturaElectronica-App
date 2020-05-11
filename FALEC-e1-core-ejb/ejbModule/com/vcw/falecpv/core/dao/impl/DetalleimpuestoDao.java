/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalleimpuestoDao extends AppGenericDao<Detalleimpuesto, String> {

	/**
	 * @param type
	 */
	public DetalleimpuestoDao() {
		super(Detalleimpuesto.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public int eliminarByCabecera(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM detalleimpuesto WHERE iddetalle in (SELECT iddetalle FROM detalle WHERE idcabecera=:idcabecera )");
			q.setParameter("idcabecera", idCabecera);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
