/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCComprobanteEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGPagos;
import com.vcw.falecpv.core.dao.impl.PagoDao;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.query.PagosQuery;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class PagoServicio extends AppGenericService<Pago, String> {

	@Inject
	private PagoDao pagoDao;
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public PagoServicio() {
	}

	@Override
	public List<Pago> consultarActivos() {
		return null;
	}

	@Override
	public List<Pago> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Pago, String> getDao() {
		return pagoDao;
	}

	/**
	 * @return the pagoDao
	 */
	public PagoDao getPagoDao() {
		return pagoDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<PagosQuery> getPagos(String idEstablecimiento,Date desde, Date hasta,Tipopago tipopago)throws DaoException{
		try {
			String tipoPagoNoIncluido = parametroGenericoServicio.consultarParametro(PGPagos.REPORTE_PAGOS_NOINCLUIR, TipoRetornoParametroGenerico.STRING);
			
			String sql = "select " + 
			"		c.idestablecimiento, " +
			"		c.updated, " +
			"		c.fechaemision, " +
			"		c.idcliente, " +
			"		cl.razonsocial, " +
			"		cl.identificacion, " +
			"		tc.idtipocomprobante, " +
			"		tc.comprobante, " +
			"		c.idcabecera, " +
			"		c.numdocumento, " +
			"		c.totalconimpuestos, " +
			"		c.valorretenido, " +
			"		c.valorapagar, " +
			"		p.idpago, " +
			"		p.idtipopago, " +
			"		tp.descripcion, " +
			"		tp.nombre, " +
			"		p.total, " +
			"		p.valorentrega, " +
			"		p.cambio, " +
			"		p.numerodocumento, " +
			"		p.nombrebanco  " +
			"	from  " +
			"		cabecera c inner join pago p on c.idcabecera = p.idcabecera " +
			"		inner join  cliente cl on cl.idcliente = c.idcliente  " +
			"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
			"		inner join tipopago tp on tp.idtipopago = p.idtipopago  " +
			"	where " +
			"		c.estado not in ('ANULADO','BORRADOR') " +
			"		and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
			"		and c.idestablecimiento = '" + idEstablecimiento + "' " +
			"		and tp.idtipopago not in (" + tipoPagoNoIncluido + ") " +
			(tipopago!=null?" and p.idtipopago='" + tipopago.getIdtipopago() +"' ":" ") +
			"	order by  " +
			"		c.fechaemision, " +
			"		cl.razonsocial, " + 
			"	    c.idcabecera   ";
			
			return resultList(sql, PagosQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<PagosQuery> getPagosTotal(String idEstablecimiento,Date desde, Date hasta,Tipopago tipopago)throws DaoException{
		try {
			String tipoPagoNoIncluido = parametroGenericoServicio.consultarParametro(PGPagos.REPORTE_PAGOS_NOINCLUIR, TipoRetornoParametroGenerico.STRING);
			
			String sql = "select " + 
					"		p.idtipopago, " +
					"		tp.nombre, " +
					"		SUM(p.total) as total, " +
					"		SUM(p.valorentrega) as valorentrega, " +
					"		SUM(p.cambio) as cambio  " +
					"	from  " +
					"		cabecera c inner join pago p on c.idcabecera = p.idcabecera " +
					"		inner join  cliente cl on cl.idcliente = c.idcliente  " +
					"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " +
					"		inner join tipopago tp on tp.idtipopago = p.idtipopago  " +
					"	where " +
					"		c.estado <> 'ANULADO' " +
					"		and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
					"		and c.idestablecimiento = '" + idEstablecimiento + "' " +
					"		and tp.idtipopago not in (" + tipoPagoNoIncluido + ") " +
					(tipopago!=null?" and p.idtipopago='" + tipopago.getIdtipopago() +"' ":" ") +
					"	group by " +
					"		p.idtipopago, " +
					"		tp.nombre " +
					"	order by  " +
					"		tp.nombre  ";
					
			
			return resultList(sql, PagosQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param pagoList
	 * @return
	 * @throws DaoException
	 */
	public List<Pago> guardarPago(List<Pago> pagoList,List<Pago> pagoEliminarList)throws DaoException{
		try {
			
			if(pagoEliminarList!=null) {
				for (Pago pe : pagoEliminarList) {
					eliminar(pe);
				}
			}
			
			for (Pago pago : pagoList) {
				if(pago.getIdpago()==null || pago.getIdpago().contains("M")) {
					String idestablecimeinto = null;
					if(pago.getCabecera()!=null) {
						idestablecimeinto = pago.getCabecera().getEstablecimiento().getIdestablecimiento();
					}
					if(pago.getAdquisicion()!=null) {
						idestablecimeinto = pago.getAdquisicion().getEstablecimiento().getIdestablecimiento();
					}
					if(idestablecimeinto!=null) {
						pago.setIdpago(contadorPkServicio.generarContadorTabla(TCComprobanteEnum.PAGO, idestablecimeinto));
						crear(pago);
					}
				}else {
					actualizar(pago);
				}
			}
			
			return pagoList;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param fecha
	 * @param noEstados
	 * @param idTipoCompobantes
	 * @param tipoPagoSplit
	 * @return
	 * @throws DaoException
	 */
	public BigDecimal getTotalPago(Date fecha, List<String> noEstados, List<String> idTipoCompobantes, String tipoPagoSplit, String idestablecimiento)throws DaoException{
		try {
			
			String noEstadosSplit = noEstados == null || noEstados.isEmpty() ? null : 
				noEstados.stream().collect(Collectors.joining("','", "'", "'"));
			
			String comprobantesSplit = idTipoCompobantes == null || idTipoCompobantes.isEmpty() ? null : 
				idTipoCompobantes.stream().collect(Collectors.joining("','", "'", "'"));
			
			String sql = "select " + 	
						"		c.fechaemision, " +
						"		sum(p.total) as total " +
						"	from cabecera c inner join pago p on c.idcabecera = p.idcabecera " + 
						"	where  " +
						(noEstadosSplit != null ? "		c.estado not in (" + noEstadosSplit + ") " : " ") +
						"		and c.fechaemision = '"  + formatoFecha(fecha) + "' " +
						(tipoPagoSplit !=null ? "		and p.idtipopago in (" + tipoPagoSplit + ") " : " " ) +
						(comprobantesSplit != null ? "		and c.idtipocomprobante in (" + comprobantesSplit + ") " : " " ) +
						"   and c.idestablecimiento='" + idestablecimiento + "' " +
						"	group by " +
						"		c.fechaemision ";
			
			Map<String, Object> r = singleResultMap(sql);
			
			if(r != null && !r.isEmpty()) {
				return (BigDecimal)r.get("total");
			}
			
			return BigDecimal.ZERO;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
