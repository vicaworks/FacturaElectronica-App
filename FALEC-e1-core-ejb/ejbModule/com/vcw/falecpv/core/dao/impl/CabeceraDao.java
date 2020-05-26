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
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CabeceraDao extends AppGenericDao<Cabecera, String> {

	/**
	 * @param type
	 */
	public CabeceraDao() {
		super(Cabecera.class);
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cabecera> getByRetencionCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento)throws DaoException{
		
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante "
						+ " AND (c.proveedor.identificacion like :rucProveedor "
						+ " OR UPPER(c.proveedor.nombrecomercial) like :nombrecomercial "
						+ " OR UPPER(c.proveedor.razonsocial) like :razonsocial "
						+ " OR c.numfactura like :numfactura "
						+ " OR c.numdocumento like :numdocumento) "
						+ " AND c.establecimiento.idestablecimiento=:idEstablecimiento "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucProveedor", criteria.concat("%"));
				q.setParameter("nombrecomercial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("razonsocial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria).concat("%"));
				q.setParameter("numdocumento", "%".concat(criteria).concat("%"));
			}
			q.setParameter("idEstablecimiento", idEstablecimiento);
			q.setParameter("idtipocomprobante", GenTipoDocumentoEnum.RETENCION.getIdentificador());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
