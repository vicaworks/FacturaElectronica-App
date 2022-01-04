/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.vista.VComprobantespagarcredito;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class VComprobantespagarcreditoDao extends AppGenericDao<VComprobantespagarcredito, String> {

	/**
	 * @param type
	 */
	public VComprobantespagarcreditoDao() {
		super(VComprobantespagarcredito.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param tipocomprobante
	 * @param criterio
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<VComprobantespagarcredito> getByCuentasCobrar(String idEstablecimiento,String tipocomprobante,String criterio,String criterioProveedor)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM VComprobantespagarcredito c WHERE c.idestablecimiento=:idestablecimiento  " 
			+ (tipocomprobante!=null?" AND c.idtipocomprobante=:idtipocomprobante ":" ") 
			+ ((criterio!=null && criterio.trim().length()>0)?" AND c.numdocumento =:numdocumento ":" ")
			+ ((criterioProveedor!=null && criterioProveedor.trim().length()>0)?" AND c.identificacion =:identificacion ":" ")
			+ " AND c.abono < c.totalpago "
			+ " ORDER BY c.fechaemision");
			
			q.setParameter("idestablecimiento", idEstablecimiento);
			if(tipocomprobante!=null) {
				q.setParameter("idtipocomprobante", tipocomprobante);
			}
			if(criterio!=null&&criterio.trim().length()>0) {
				q.setParameter("numdocumento", criterio);
			}
			if(criterioProveedor!=null&&criterioProveedor.trim().length()>0) {
				q.setParameter("identificacion", criterioProveedor);
			}
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
