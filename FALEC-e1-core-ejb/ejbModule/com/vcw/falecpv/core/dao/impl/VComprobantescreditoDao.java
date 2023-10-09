/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
	public List<VComprobantescredito> getByCuentasCobrar(String idEstablecimiento,
			String idTipoComprobante,
			List<Tipocomprobante> tipocomprobanteList,
			String criterio,
			String criterioCliente,
			Date desde,
			Date hasta)throws DaoException{
		try {
			List<String> comprobantes = new ArrayList<>();
			Query q = getEntityManager().createQuery("SELECT c FROM VComprobantescredito c WHERE c.idestablecimiento=:idestablecimiento  " 
			+ (idTipoComprobante!=null?" AND c.idtipocomprobante in (:idtipocomprobante) ":" ") 
			+ ((criterio!=null && criterio.trim().length()>0)?" AND c.numdocumento =:numdocumento ":"  ")
			+ ((criterioCliente!=null && criterioCliente.trim().length()>0)?" AND c.identificacion=:identificacion ":" ")
			//+ ((criterioCliente==null || criterioCliente.trim().length()==0) && (criterio==null || criterio.trim().length()==0) ? " " )
			+ (!idTipoComprobante.equals("H") ? " AND c.abono < c.totalpago " : " ")
			+ (desde != null && idTipoComprobante.equals("H") ? " AND c.fechaemision BETWEEN :desde AND :hasta " : " ")
			+ " ORDER BY c.fechaemision ");
			//+ " ORDER BY c.fechaemision " + ((criterioCliente!=null && criterioCliente.trim().length()>0)?" DESC ":" "));
			
			q.setParameter("idestablecimiento", idEstablecimiento);
			if(idTipoComprobante!=null) {
				switch (idTipoComprobante) {
				case "H":
					comprobantes.addAll(tipocomprobanteList.stream().map(x->x.getIdtipocomprobante()).collect(Collectors.toList()));
					break;
					
				case "T":
					comprobantes.addAll(tipocomprobanteList.stream().map(x->x.getIdtipocomprobante()).collect(Collectors.toList()));
					break;

				default:
					comprobantes.add(idTipoComprobante);
					break;
				}
				q.setParameter("idtipocomprobante", comprobantes);
			}
			if(desde != null && idTipoComprobante.equals("H")) {
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
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
