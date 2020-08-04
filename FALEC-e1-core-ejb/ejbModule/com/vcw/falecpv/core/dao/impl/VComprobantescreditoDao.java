/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;
import com.vcw.falecpv.core.modelo.vista.VComprobantescredito;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class VComprobantescreditoDao extends AppGenericDao<VComprobantescredito, String> {

	/**
	 * @param type
	 */
	public VComprobantescreditoDao() {
		super(VComprobantescredito.class);
	}
	
	/**
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @param tipocomprobante
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<VComprobantescredito> getByCuentasCobrar(String idEstablecimiento,Tipocomprobante tipocomprobante)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM VComprobantescredito c WHERE c.idestablecimiento=:idestablecimiento AND c.abono < c.totalpago " + (tipocomprobante!=null?" AND c.idtipocomprobante:=idtipocomprobante ":" ") + " ORDER BY c.fechaemision");
			q.setParameter("idestablecimiento", idEstablecimiento);
			if(tipocomprobante!=null) {
				q.setParameter("idtipocomprobante", tipocomprobante.getIdtipocomprobante());
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
