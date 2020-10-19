/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ComprobanterecibidoDao extends AppGenericDao<Comprobanterecibido, String> {

	/**
	 * @param type
	 */
	public ComprobanterecibidoDao() {
		super(Comprobanterecibido.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param tipoComprobante
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Comprobanterecibido> getByComprobanteEmpresa(String idEmpresa, GenTipoDocumentoEnum tipoComprobante,
			Date desde, Date hasta, String criteria) throws DaoException {
		
		try {
			
			Query q = null;
			
			if(criteria!=null && criteria.trim().length()>0) {
				
				q = getEntityManager().createQuery("SELECT c FROM Comprobanterecibido c WHERE c.empresa.idempresa=:empresa AND c.tipocomprobante.identificador= :tipocomprobante AND (c.rucEmisor like :rucEmisor OR UPPER(c.razonSocialEmisor) like :razonSocialEmisor OR c.serieComprobante like :serieComprobante) ORDER BY c.fechaEmision");
				
				q.setParameter("rucEmisor", "%".concat(criteria).concat("%"));
				q.setParameter("razonSocialEmisor", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("serieComprobante", "%".concat(criteria).concat("%"));
				
			}else {
				
				q = getEntityManager().createQuery("SELECT c FROM Comprobanterecibido c WHERE c.empresa.idempresa=:empresa AND  c.fechaEmision BETWEEN :fechaIni AND :fechaHasta AND c.tipocomprobante.identificador= :tipocomprobante ORDER BY c.fechaEmision");
				
				q.setParameter("fechaIni", desde);
				q.setParameter("fechaHasta", hasta);
				
			}
			
			q.setParameter("tipocomprobante", tipoComprobante.getIdentificador());
			q.setParameter("empresa", idEmpresa);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

}
