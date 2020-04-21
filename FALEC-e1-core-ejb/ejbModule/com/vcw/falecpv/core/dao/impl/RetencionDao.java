/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionDao extends AppGenericDao<Retencion, String> {

	/**
	 * @param type
	 */
	public RetencionDao() {
		super(Retencion.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Retencion> getByCriteria(String idEstablecimiento,Date desde,Date hasta,String criteria)throws DaoException{
		try {
			
			Query q = null;
			if(criteria!=null && criteria.trim().length()>0) {
				q = getEntityManager().createQuery("SELECT r FROM Retencion r WHERE r.establecimiento.idestablecimiento=:idestablecimiento  AND (r.proveedor.nombrecomercial like :proveedor OR r.proveedor.identificacion like :ruc OR r.numcomprobante like :numcomprobante OR r.numfactura like :numfactura ) ORDER BY r.fechaemision DESC");
				q.setParameter("proveedor", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numcomprobante", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("ruc", "%".concat(criteria.toUpperCase()).concat("%"));
			}else {
				q = getEntityManager().createQuery("SELECT r FROM Retencion r WHERE r.establecimiento.idestablecimiento=:idestablecimiento  AND r.fechaemision BETWEEN :desde AND :hasta ORDER BY r.fechaemision DESC");
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
			}
			
			q.setParameter("idestablecimiento", idEstablecimiento);
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
