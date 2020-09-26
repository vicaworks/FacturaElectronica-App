/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;

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
	public List<Cabecera> getByRetencionCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,String estado)throws DaoException{
		
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				
				if(estado!=null) {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado"  +  (estado.equals("I")?"=":"<>") +  "'ANULADO' AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}else {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE  c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}
				
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante "
						+ " AND (c.cliente.identificacion like :rucProveedor "
						+ " OR UPPER(c.cliente.razonsocial) like :razonsocial "
						+ " OR c.numfactura like :numfactura "
						+ " OR c.numdocumento like :numdocumento) "
						+ " AND c.establecimiento.idestablecimiento=:idEstablecimiento "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucProveedor", criteria.concat("%"));
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
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @param estado
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cabecera> getByLiqCompraCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,String estado)throws DaoException{
		
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				
				if(estado!=null) {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado"  +  (estado.equals("I")?"=":"<>") +  "'ANULADO' AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}else {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE  c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}
				
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante "
						+ " AND (c.cliente.identificacion like :rucProveedor "
						+ " OR UPPER(c.cliente.razonsocial) like :razonsocial "
						+ " OR c.numfactura like :numfactura "
						+ " OR c.numdocumento like :numdocumento) "
						+ " AND c.establecimiento.idestablecimiento=:idEstablecimiento "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucProveedor", criteria.concat("%"));
				q.setParameter("razonsocial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria).concat("%"));
				q.setParameter("numdocumento", "%".concat(criteria).concat("%"));
			}
			q.setParameter("idEstablecimiento", idEstablecimiento);
			q.setParameter("idtipocomprobante", GenTipoDocumentoEnum.LIQUIDACION_COMPRA.getIdentificador());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
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
	public List<Cabecera> getByNotaCreditoCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,String estado)throws DaoException{
		
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				
				if(estado!=null) {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado" + (estado.equals("I")?"=":"<>") + "'ANULADO' AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}else {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante "
						+ " AND (c.cliente.identificacion like :rucCliente "
						+ " OR UPPER(c.cliente.razonsocial) like :nombrecliente "
						+ " OR c.numdocumento like :numfactura "
						+ " OR c.numdocasociado like :numdocumento) "
						+ " AND c.establecimiento.idestablecimiento=:idEstablecimiento "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucCliente", criteria.concat("%"));
				q.setParameter("nombrecliente", "%".concat(criteria.toUpperCase()).concat("%"));
//				q.setParameter("razonsocial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria).concat("%"));
				q.setParameter("numdocumento", "%".concat(criteria).concat("%"));
			}
			q.setParameter("idEstablecimiento", idEstablecimiento);
			q.setParameter("idtipocomprobante", GenTipoDocumentoEnum.NOTA_CREDITO.getIdentificador());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @param estado
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cabecera> getByCotizacionCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,String estado)throws DaoException{
		
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				
				if(estado!=null) {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado=:estado AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
					q.setParameter("estado", estado);
				}else {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante "
						+ " AND (c.cliente.identificacion like :rucCliente "
						+ " OR UPPER(c.cliente.razonsocial) like :nombrecliente "
						+ " OR c.numdocumento like :numfactura) "
						+ " AND c.establecimiento.idestablecimiento=:idEstablecimiento "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucCliente", criteria.concat("%"));
				q.setParameter("nombrecliente", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria).concat("%"));
			}
			q.setParameter("idEstablecimiento", idEstablecimiento);
			q.setParameter("idtipocomprobante", GenTipoDocumentoEnum.COTIZACION.getIdentificador());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param tipocomprobante
	 * @param numDocumento
	 * @param idCliente
	 * @return
	 * @throws DaoException
	 */
	public Cabecera getByTipoComprobante(String idEstablecimiento, Tipocomprobante tipocomprobante, String numDocumento) throws DaoException {
		
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM Cabecera c "
					+ " WHERE c.establecimiento.idestablecimiento=:idestablecimiento "
					+ " AND c.tipocomprobante.idtipocomprobante=:idtipocomprobante "
					+ " AND c.numdocumento=:numdocumento "
					+ " AND c.estado<>'ANULADO' ");
			
			q.setParameter("idtipocomprobante", tipocomprobante.getIdtipocomprobante());
			q.setParameter("numdocumento", numDocumento);
			q.setParameter("idestablecimiento", idEstablecimiento);
			
			@SuppressWarnings("unchecked")
			List<Cabecera> lista = q.getResultList();
			if(lista.size()>0) {
				return lista.get(0);
			}
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cabecera> getByIds(List<String> idList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.idcabecera in :idList ORDER BY c.fechaemision,c.cliente.razonsocial");
			q.setParameter("idList", idList);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idList
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cabecera> getByIdsTransportista(List<String> idList)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.idcabecera in :idList ORDER BY c.fechaemision,c.transportista.razonsocial");
			q.setParameter("idList", idList);
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idcabecera
	 * @return
	 * @throws DaoException
	 */
	public int anularById(String idCabecera)throws DaoException{
		try {
			
			String sql = "UPDATE cabecera SET estado='" + ComprobanteEstadoEnum.ANULADO.toString() + "' WHERE idcabecera='" + idCabecera + "'";
			return getEntityManager().createNativeQuery(sql).executeUpdate();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idFactura
	 * @param idGuiaRemision
	 * @return
	 * @throws DaoException
	 */
	public int asignarRefGuiaRemicion(String idFactura,String idGuiaRemision)throws DaoException{
		try {
			
			String sql = "UPDATE cabecera SET idguiaremision=:idGuiaRemision WHERE idcabecera=:idFactura";
			Query q = getEntityManager().createNativeQuery(sql);
			q.setParameter("idGuiaRemision", idGuiaRemision);
			q.setParameter("idFactura", idFactura);
			
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @param estado
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Cabecera> getByNotDebitoCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,String estado)throws DaoException{
		
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				
				if(estado!=null) {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado"  +  (estado.equals("I")?"=":"<>") +  "'ANULADO' AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}else {
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE  c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
				}
				
				q.setParameter("desde", desde);
				q.setParameter("hasta", hasta);
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante "
						+ " AND (c.cliente.identificacion like :rucCliente "
						+ " OR UPPER(c.cliente.razonsocial) like :razonsocial "
						+ " OR c.numdocasociado like :numfactura "
						+ " OR c.numdocumento like :numdocumento) "
						+ " AND c.establecimiento.idestablecimiento=:idEstablecimiento "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucCliente", criteria.concat("%"));
				q.setParameter("razonsocial", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria).concat("%"));
				q.setParameter("numdocumento", "%".concat(criteria).concat("%"));
			}
			q.setParameter("idEstablecimiento", idEstablecimiento);
			q.setParameter("idtipocomprobante", GenTipoDocumentoEnum.NOTA_DEBITO.getIdentificador());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
