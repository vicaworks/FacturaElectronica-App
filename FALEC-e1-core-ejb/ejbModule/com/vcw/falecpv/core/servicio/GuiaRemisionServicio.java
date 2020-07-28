/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.dao.impl.DestinatarioDao;
import com.vcw.falecpv.core.dao.impl.DetalledestinatarioDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;
import com.vcw.falecpv.core.util.SqlUtil;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class GuiaRemisionServicio extends DBUtilGenericoApp {

	@Inject
	private CabeceraDao cabeceraDao;
	
	@Inject
	private DestinatarioDao destinatarioDao;
	
	@Inject
	private DetalledestinatarioDao detalledestinatarioDao;
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstableciiento
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @return
	 * @throws DaoException
	 */
	public List<Cabecera> getByDateCriteria(String idEstableciiento,Date desde,Date hasta,String criteria,String estado)throws DaoException{
		try {
			
			String sql = "select " +	
				"	 distinct  " +	
				"	 c.idcabecera " + 
				"	from  " +
				"		cabecera c inner join transportista t on c.idtransportista = t.idtransportista " +
				"       inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
				"		inner join destinatario d on c.idcabecera = d.idcabecera  " +
				"		inner join cliente cl on cl.idcliente = d.idcliente  " +
				"	where  " +
				"       tc.identificador ='" + GenTipoDocumentoEnum.GUIA_REMISION.getIdentificador() + "' " +
				"		and c.idestablecimiento = '" + idEstableciiento + "' ";
			
			sql += "		and (c.fechainiciotransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' or c.fechafintransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "') ";
			
			
//			if(criteria==null || criteria.trim().isEmpty()) {
//				
//				sql += "		and (c.fechainiciotransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' or c.fechafintransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "') ";
//			}
			
			if(estado!=null) {
				sql += "    and c.estado " + (estado.equals("I")?"=":"<>") + "'ANULADO' ";
			}
			
			if(criteria!=null && !criteria.trim().isEmpty()) {
				
				sql +=  "		and (" +
						"			UPPER(c.direccionpartida) like '%" + criteria.toUpperCase()  + "%' " +		
						"			or		 " +
						"			c.numdocumento like '%" + criteria  + "%' " +
						"			or		 " +
						"			d.numdocsustento like '%" + criteria  + "%' " +
						"			or " +
						"			UPPER(t.razonsocial) like '%" + criteria.toUpperCase()  + "%' " +
						"			or " +
						"			t.identificacion like '%" + criteria  + "%' " +
						"			or " +
						"			UPPER(d.razonsocialdestinatario) like '%" + criteria.toUpperCase()  + "%' " +
						"			or " +
						"			UPPER(d.ruta) like '%" + criteria.toUpperCase()  + "%' " +
						"			or " +
						"			cl.identificacion like '%" + criteria.toUpperCase()  + "%') ";
				
			}
			
			List<Map<String, Object>> idListMap= resultListMap(sql);
			List<String> idList = idListMap.stream().map(x->(String)x.get("idcabecera")).collect(Collectors.toList());
			if(idList.isEmpty())
				return new ArrayList<>();
			
			// consulta cabeceras en base a los ids
			List<Cabecera> cabeceraList = cabeceraDao.getByIdsTransportista(idList);
			// consulta todos los destinatarios en base a los ids
			List<Destinatario> destinatarioList = destinatarioDao.getIdDestinatarioByCabeceraIdList(idList);
			// filtra id destinatarios
			List<String> idDestinatarioList = destinatarioList.stream().map(x->x.getIddestinatario()).collect(Collectors.toList());
			// consulta detalle de todos los destinatarios
			List<Detalledestinatario> detalledestinatarioList = detalledestinatarioDao.getByIdListDestinatario(idDestinatarioList);
			
			// 1. unifica las consultas
			// 2. el detalle en cada destinatario
			destinatarioList.forEach(x->{
				x.setDetalledestinatarioList(detalledestinatarioList.stream().filter(y->y.getDestinatario().getIddestinatario().equals(x.getIddestinatario())).collect(Collectors.toList()));
			});
			// 3. el destinatario en cada cabecera
			for (Cabecera gr : cabeceraList) {
				gr.setDestinatarioList(destinatarioList.stream().filter(y->y.getCabecera().getIdcabecera().equals(gr.getIdcabecera())).collect(Collectors.toList()));
			}
			
			return cabeceraList;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstableciiento
	 * @param desde
	 * @param hasta
	 * @param criteria
	 * @param estado
	 * @return
	 * @throws DaoException
	 */
	public List<Destinatario> getGRByDateCriteria(String idEstableciiento,Date desde,Date hasta,String criteria,String estado)throws DaoException{
		try {
			
			String sql = "select " +	
					"	 distinct  " +	
					"	 d.iddestinatario " + 
					"	from  " +
					"		cabecera c inner join transportista t on c.idtransportista = t.idtransportista " +
					"       inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
					"		inner join destinatario d on c.idcabecera = d.idcabecera  " +
					"		inner join cliente cl on cl.idcliente = d.idcliente  " +
					"	where  " +
					"       tc.identificador ='" + GenTipoDocumentoEnum.GUIA_REMISION.getIdentificador() + "' " +
					"		and c.idestablecimiento = '" + idEstableciiento + "' ";
				
				sql += "		and (c.fechainiciotransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' or c.fechafintransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "') ";
				
				
//				if(criteria==null || criteria.trim().isEmpty()) {
//					
//					sql += "		and (c.fechainiciotransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' or c.fechafintransporte between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "') ";
//				}
				
				if(estado!=null) {
					sql += "    and c.estado " + (estado.equals("I")?"=":"<>") + "'ANULADO' ";
				}
				
				if(criteria!=null && !criteria.trim().isEmpty()) {
					
					sql +=  "		and (" +
							"			UPPER(c.direccionpartida) like '%" + criteria.toUpperCase()  + "%' " +		
							"			or		 " +
							"			c.numdocumento like '%" + criteria  + "%' " +
							"			or		 " +
							"			d.numdocsustento like '%" + criteria  + "%' " +
							"			or " +
							"			UPPER(t.razonsocial) like '%" + criteria.toUpperCase()  + "%' " +
							"			or " +
							"			t.identificacion like '%" + criteria  + "%' " +
							"			or " +
							"			UPPER(d.razonsocialdestinatario) like '%" + criteria.toUpperCase()  + "%' " +
							"			or " +
							"			UPPER(d.ruta) like '%" + criteria.toUpperCase()  + "%' " +
							"			or " +
							"			cl.identificacion like '%" + criteria.toUpperCase()  + "%') ";
					
				}
				
				List<Map<String, Object>> idListMap= resultListMap(sql);
				
				List<String> idList = idListMap.stream().map(x->(String)x.get("iddestinatario")).collect(Collectors.toList());
				List<Destinatario> destinatarioList = destinatarioDao.getById(idList);
				//filtra id destinatarios
				List<String> idDestinatarioList = destinatarioList.stream().map(x->x.getIddestinatario()).collect(Collectors.toList());
				// consulta detalle de todos los destinatarios
				List<Detalledestinatario> detalledestinatarioList = detalledestinatarioDao.getByIdListDestinatario(idDestinatarioList);
				// 1. unifica las consultas
				// 2. el detalle en cada destinatario
				destinatarioList.forEach(x->{
					x.setDetalledestinatarioList(detalledestinatarioList.stream().filter(y->y.getDestinatario().getIddestinatario().equals(x.getIddestinatario())).collect(Collectors.toList()));
				});
				
			
			return destinatarioList;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idretencion
	 * @param idEstablecimiento
	 * @param accion
	 * @return
	 * @throws DaoException
	 */
	public String analizarEstado(String idretencion,String idEstablecimiento,String accion)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(cabeceraDao.getEntityManager());
			Cabecera r = (Cabecera) q.select("r")
							.from(Cabecera.class,"r")
							.equals("r.idcabecera",idretencion)
							.equals("r.establecimiento.idestablecimiento",idEstablecimiento).getSingleResult();
			
			if(r==null) {
				return "NO EXISTE LA GUIA REMISION";
			}
			
			if(r!=null && accion.equals("ANULAR")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE ANULAR, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("ELIMINAR_DETALLE")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE ELIMINAR, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			if(r!=null && accion.equals("GUARDAR")) {
				List<ComprobanteEstadoEnum> lista = new ArrayList<>();
				lista.add(ComprobanteEstadoEnum.ANULADO);
				lista.add(ComprobanteEstadoEnum.AUTORIZACION);
				lista.add(ComprobanteEstadoEnum.SRI);
				
				if(lista.contains(ComprobanteEstadoEnum.getByEstado(r.getEstado()))) {
					return "NO SE PUEDE REALIZAR NINGUNA MODIFICACION, POR QUE SE ENCUENTRA EN ESTADO: " + r.getEstado();
				}
				
			}
			
			
			return null;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	
}
