/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
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
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado=:estado AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
					q.setParameter("estado", estado);
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
						+ " OR c.numdocumento like :numfactura "
						+ " OR c.numdocasociado like :numdocumento) "
						+ " AND c.establecimiento.idestablecimiento=:idEstablecimiento "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucCliente", criteria.concat("%"));
				q.setParameter("nombrecliente", "%".concat(criteria.toUpperCase()).concat("%"));
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
	 * 
	 * @param idEmpresa
	 * @param idusuario
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param idEstablecimiento
	 * @param estadoList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Cabecera> getByCotizacionCriteria(String idEmpresa,String idusuario,String idCliente ,Date desde,Date hasta,String criteria,String idEstablecimiento,List<String> estadoList){
		
		String sql = "SELECT c FROM Cabecera c WHERE c.estado in :estado AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta " +
				(idEstablecimiento!=null?" AND c.establecimiento.idestablecimiento=:idestablecimiento ":" AND c.establecimiento.empresa.idempresa=:idempresa ") +
				(idusuario!=null?" AND c.idusuario=:idusuario ":" ") +
				(criteria!=null && criteria.trim().length()>0?" AND c.numdocumento like :numfactura ":" ") +
				(idCliente!=null?" AND c.cliente.idcliente=:idcliente ":" ") +
				" ORDER BY c.fechaemision ASC,c.idcabecera DESC";
		Query q = getEntityManager().createQuery(sql);
		
		q.setParameter("estado", estadoList);
		q.setParameter("desde", desde);
		q.setParameter("hasta", hasta);
		q.setParameter("idtipocomprobante", GenTipoDocumentoEnum.COTIZACION.getIdentificador());
		if(idEstablecimiento==null) {
			q.setParameter("idempresa", idEmpresa);
		}else {
			q.setParameter("idestablecimiento", idEstablecimiento);
		}
		
		if(idusuario!=null) {
			q.setParameter("idusuario", idusuario);
		}
		
		if(idCliente!=null) {
			q.setParameter("idcliente", idCliente);
		}
		
		if(criteria!=null && criteria.trim().length()>0) {
			q.setParameter("numfactura", "%".concat(criteria).concat("%"));
		}
		
		
		return q.getResultList();
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
	public List<Cabecera> getByCotizacionCriteria(Date desde,Date hasta,String criteria,String idEstablecimiento,List<String> estadoList)throws DaoException{
		
		try {
			
			Query q = null;
			
			if(criteria==null || criteria.trim().isEmpty()) {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado in :estado AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
			}else {
				q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:idtipocomprobante AND c.establecimiento.idestablecimiento=:idEstablecimiento AND c.fechaemision BETWEEN :desde AND :hasta "
						+ (estadoList!=null?" AND c.estado in :estado ":"")
						+ " AND (c.cliente.identificacion like :rucCliente "
						+ " OR UPPER(c.cliente.razonsocial) like :nombrecliente "
						+ " OR c.numdocumento like :numfactura "
						+ " OR UPPER(c.usuarioconsulta.nombre) like :usuario ) "
						+ " ORDER BY c.fechaemision ASC,c.idcabecera DESC");
				q.setParameter("rucCliente", criteria.concat("%"));
				q.setParameter("nombrecliente", "%".concat(criteria.toUpperCase()).concat("%"));
				q.setParameter("numfactura", "%".concat(criteria).concat("%"));
				q.setParameter("usuario", "%".concat(criteria.toUpperCase()).concat("%"));
			}
			
			q.setParameter("estado", estadoList);
			q.setParameter("desde", desde);
			q.setParameter("hasta", hasta);
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
	 * @param idFactura
	 * @param idGuiaRemision
	 * @return
	 * @throws DaoException
	 */
	public int asignarRefGuiaRemicion(String idFactura,String idGuiaRemision)throws DaoException{
		try {
			
			if(idFactura==null || (idGuiaRemision==null || idGuiaRemision.trim().length()==0)) {
				return 0;
			}
			
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
					q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.estado=:estado AND c.tipocomprobante.identificador=:idtipocomprobante AND c.fechaemision BETWEEN :desde AND :hasta AND c.establecimiento.idestablecimiento=:idEstablecimiento ORDER BY  c.fechaemision ASC,c.idcabecera DESC");
					q.setParameter("estado", estado);
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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param numDocumento
	 * @return
	 * @throws DaoException
	 */
	public String getFacturaGuiaRemision(String idEstablecimiento,String numDocumento)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT c FROM Cabecera c WHERE c.tipocomprobante.identificador=:identificador AND c.establecimiento.idestablecimiento=:idEstablecimineto AND c.estado <> 'ANULADO' AND c.numdocumento=:numDocumento");
			q.setParameter("idEstablecimineto", idEstablecimiento);
			q.setParameter("numDocumento", numDocumento);
			q.setParameter("identificador", GenTipoDocumentoEnum.FACTURA.getIdentificador());
			
			@SuppressWarnings("unchecked")
			List<Cabecera> rs = q.getResultList();
			if(rs.isEmpty()) {
				return null;
			}
			return rs.get(0).getIdcabecera();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 */
	public boolean isImprimir(String idCabecera) {
		try {
			Query q = getEntityManager().createNativeQuery("SELECT c.idcabecera FROM Cabecera c WHERE c.idcabecera=:id AND c.estado not in :lista");
			q.setParameter("id", idCabecera);
			q.setParameter("lista", Arrays.asList(new String[] {"BORRADOR","ANULADO"}));
			
			return q.getResultList().size()>0;					
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public String getIdClienteComprobante(String idCabecera)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("SELECT c.idcliente FROM cabecera c where c.idcabecera=:id");
			q.setParameter("id", idCabecera);
			return (String) q.getSingleResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param estado
	 * @throws DaoException
	 */
	public void setEstadoEmailComprobante(String idCabecera,int estado)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("UPDATE cabecera set envioemail=:estado where idcabecera=:idcabecera");
			q.setParameter("idcabecera", idCabecera);
			q.setParameter("estado", estado);
			q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
