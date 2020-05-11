/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

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

}
