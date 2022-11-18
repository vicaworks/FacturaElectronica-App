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
	public List<VComprobantescredito> getByCuentasCobrar(String idEstablecimiento,Tipocomprobante tipocomprobante,String criterio,String criterioCliente)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM VComprobantescredito c WHERE c.idestablecimiento=:idestablecimiento  " 
			+ (tipocomprobante!=null?" AND c.idtipocomprobante=:idtipocomprobante ":" ") 
			+ ((criterio!=null && criterio.trim().length()>0)?" AND c.numdocumento =:numdocumento ":"  ")
			+ ((criterioCliente!=null && criterioCliente.trim().length()>0)?" AND c.identificacion=:identificacion ":" ")
			+ ((criterioCliente==null || criterioCliente.trim().length()==0) && (criterio==null || criterio.trim().length()==0)?" AND c.abono < c.totalpago ":" ")
			+ " ORDER BY c.fechaemision " + ((criterioCliente!=null && criterioCliente.trim().length()>0)?" DESC ":" "));
			
			q.setParameter("idestablecimiento", idEstablecimiento);
			if(tipocomprobante!=null) {
				q.setParameter("idtipocomprobante", tipocomprobante.getIdtipocomprobante());
			}
			if(criterio!=null&&criterio.trim().length()>0) {
				q.setParameter("numdocumento", criterio);
			}
			if(criterioCliente!=null && criterioCliente.trim().length()>0) {
				q.setParameter("identificacion", criterioCliente);
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
