/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TotalimpuestoDao extends AppGenericDao<Totalimpuesto, String> {

	/**
	 * @param type
	 */
	public TotalimpuestoDao() {
		super(Totalimpuesto.class);
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
			
			Query q = getEntityManager().createNativeQuery("DELETE FROM totalimpuesto WHERE idcabecera =:idcabecera");
			q.setParameter("idcabecera", idCabecera);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
