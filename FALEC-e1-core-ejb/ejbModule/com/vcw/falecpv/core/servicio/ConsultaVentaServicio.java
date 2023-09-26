/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
import com.vcw.falecpv.core.modelo.persistencia.Grupocategoria;
import com.vcw.falecpv.core.modelo.persistencia.Producto;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.util.SqlUtil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ConsultaVentaServicio extends DBUtilGenericoApp {

	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param criterio
	 * @param desde
	 * @param hasta
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getFacturasEmitidas(String estado,String idusuario,String criterio,Date desde,Date hasta,String idEstablecimiento,GenTipoDocumentoEnum genTipoDocumentoEnum)throws DaoException{
		try {
			
			String sql = "select " + 
						"	c.idcabecera, " +
						"	c.secuencial, " +
						"	c.fechaemision, " +
						"	c.idcliente, " +
						"	cl.identificacion, " +
						"	cl.razonsocial, " +
						"	u.nombrepantalla, " +
						"	(select SUM(d.cantidad) from detalle d where d.idcabecera = c.idcabecera ) as cantidad, " +
						"	c.totalsinimpuestos, " +
						"	c.totaliva iva, " +
						"	c.totalice ice,  " +
						"   c.totaldescuento, " +
						"	c.totalconimpuestos as total, " +
						"	c.estado, " +
						"	c.estadoautorizacion, " +
						"	c.idguiaremision, " +
						"	c.numdocumento, " +
						"	c.resumenpago, " +
						"	c.envioemail, " +
						"	c.valorretenido, " +
						"	c.valorapagar, " +
						"	(select coalesce(SUM(p.valorentrega),0) from pago p where p.idcabecera = c.idcabecera ) as licitado, " +
						"	(select coalesce(SUM(p.cambio),0) from pago p where p.idcabecera = c.idcabecera ) as cambio, " +
						"   (select coalesce(SUM(p.total),0) from pago p where p.idcabecera = c.idcabecera ) as totalpago " +
					"	from " +
					"		cabecera c inner join cliente cl on cl.idcliente =c.idcliente " + 
					"		inner join tipocomprobante  tc on tc.idtipocomprobante =c.idtipocomprobante " +
					"		inner join usuario u on u.idusuario = c.idusuario  " +
					"	where  " +
					"		c.fechaemision between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' " +
					(estado!=null?"		and c.estado = '" + estado + "' ":" ")  +
					"		and c.idestablecimiento = '" + idEstablecimiento + "' " +
					"		and tc.identificador = '" +  genTipoDocumentoEnum.getIdentificador() + "' " ;
			
			if(criterio!=null && !criterio.trim().isEmpty()) {
				sql +=  "		and (cl.razonsocial like '%" + criterio  + "%' " +
						"		or c.numdocumento like '%" +  criterio + "%') ";
				
			}
			
			if(idusuario!=null) {
				sql += " and u.idusuario = '" + idusuario + "' ";
			}
					
			sql += "	order by  " +
					"		c.fechaemision, " +
					"		cl.razonsocial  ";

			return resultList(sql, VentasQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param comprobanteEstadoEnum
	 * @param criterio
	 * @param desde
	 * @param hasta
	 * @param idEstablecimiento
	 * @param genTipoDocumentoEnum
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getFacturasEmitidas(Usuario usuario, ComprobanteEstadoEnum comprobanteEstadoEnum, String criterio,
			Date desde, Date hasta, String idEstablecimiento, String idEmpresa, GenTipoDocumentoEnum genTipoDocumentoEnum)
			throws DaoException {
		try {
			
			String sql = "select " + 
						"	c.idcabecera, " +
						"	c.secuencial, " +
						"	c.fechaemision, " +
						"	est.codigoestablecimiento, " +
						"	cl.identificacion, " +
						"	c.idcliente, " +
						"	cl.razonsocial, " +
						"	u.nombrepantalla, " +
						"	tc.identificador, " +
						"	(select SUM(d.cantidad) from detalle d where d.idcabecera = c.idcabecera ) as cantidad, " +
						"	c.totalsinimpuestos, " +
						"	c.totaliva iva, " +
						"	c.totalice ice,  " +
						"   c.totaldescuento, " +
						"	c.totalconimpuestos as total, " +
						"	c.estado, " +
						"	c.estadoautorizacion, " +
						"	c.numdocumento, " +
						"	c.resumenpago, " +
						"	c.envioemail, " +
						"	c.valorretenido, " +
						"	c.valorapagar, " +
						"	(select SUM(p.valorentrega ) from pago p where p.idcabecera = c.idcabecera ) as licitado, " +
						"	(select SUM(p.cambio ) from pago p where p.idcabecera = c.idcabecera ) as cambio, " +
						"   (select SUM(p.total ) from pago p where p.idcabecera = c.idcabecera ) as totalpago " +
					"	from " +
					"		cabecera c inner join cliente cl on cl.idcliente =c.idcliente " +
					"		inner join establecimiento est on est.idestablecimiento = c.idestablecimiento " +
					"		inner join tipocomprobante  tc on tc.idtipocomprobante =c.idtipocomprobante " +
					"		inner join usuario u on u.idusuario = c.idusuario  " +
					"	where  " +
					"		c.fechaemision between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' " +
					( idEstablecimiento!=null?"		and c.idestablecimiento  = '" + idEstablecimiento + "' ":
						"		and est.idempresa  = '" + idEmpresa + "' ") +
					"		and tc.identificador = '" +  genTipoDocumentoEnum.getIdentificador() + "' " +
				    (comprobanteEstadoEnum.equals(ComprobanteEstadoEnum.ANULADO)?" and c.estado = 'ANULADO' ":" and c.estado not in ('ANULADO','BORRADOR') ") + " ";
			
			
			if(usuario!=null) {
				sql += "       and u.idusuario = '" + usuario.getIdusuario() + "' "; 
			}
			
			if(criterio!=null && !criterio.trim().isEmpty()) {
				sql +=  "		and (cl.razonsocial like '%" + criterio  + "%' " +
						"		or c.numdocumento like '%" +  criterio + "%') ";
			}
			
			sql += "	order by  " +
					"		c.fechaemision, " +
					"		cl.razonsocial  ";

			return resultList(sql, VentasQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param usuario
	 * @param tipopago
	 * @param fabricante
	 * @param categoria
	 * @param idEstablecimiento
	 * @param criterio
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getVentasDetalleCriterio(Usuario usuario, Tipopago tipopago, Fabricante fabricante,
			Grupocategoria grupocategoriaSelected,
			Categoria categoria, 
			String idEstablecimiento, 
			String idEmpresa, 
			String criterio, 
			Date desde, 
			Date hasta)
			throws DaoException {
		try {
			
			String sql =  "select " +
							"	DISTINCT " +
							"	d.iddetalle, " +
							"	c.idcabecera, " +
							"	c.idtipocomprobante, " +
							"	c.estado, " +
							"	est.codigoestablecimiento, " +
							"	tc.identificador, " + 
							"	c.secuencial, " +
							"	c.fechaemision, " +
							"	c.idcliente, " +
							"	cl.identificacion, " +
							"	cl.razonsocial, " +
							"	d.idproducto, " +
							"   p.codigoprincipal, " +
							"   f.nombrecomercial as fabricante, " +
							"   ca.descripcion as categoria, " +
							"	d.descripcion, " +
							"	d.cantidad, " +
							"	d.preciounitario, " +
							"	d.descuento as totaldescuento, " +
							"	d.preciototalsinimpuesto as totalsinimpuestos, " +
							"	d.idiva, " +
							"	iva.valor as porcentajeiva, " +
							"	d.valoriva, " +
							"	d.idice, " +
							"	ice.valor as porcentajeice, " +
							"	d.valorice,	 " +
							"	d.preciototal,  " +
							"   u.nombrepantalla, " +
							"   c.estadoautorizacion, " +
							"   c.numdocumento " +
						"	from  " +
						"		cabecera c inner join cliente cl on cl.idcliente = c.idcliente " +
						"		inner join establecimiento est on est.idestablecimiento = c.idestablecimiento " +
						"		inner join tipocomprobante  tc on tc.idtipocomprobante =c.idtipocomprobante " +	 
						"		inner join usuario u on u.idusuario = c.idusuario " +
						"		inner join detalle d on d.idcabecera = c.idcabecera " +
						"		left join producto p on p.idproducto = d.idproducto " +
						"       left join fabricante f on p.idfabricante = f.idfabricante " + 
						"       left join categoria ca on ca.idcategoria = p.idcategoria " +
						"       left join grupocategoria gc on gc.idgrupocategoria = ca.idgrupocategoria  " +
						"		inner join iva on iva.idiva = d.idiva  " +
						"		inner join ice on ice.idice  = d.idice  " +
						"	where " +
						"		c.fechaemision between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' " +
						( idEstablecimiento!=null?"		and c.idestablecimiento  = '" + idEstablecimiento + "' ":
							"		and est.idempresa  = '" + idEmpresa + "' ") +
						"		and tc.identificador in ('00','01') " +
						"		and c.estado not in ('ANULADO','BORRADOR') ";
						
						
				if(usuario!=null) {
					sql +=	"		and c.idusuario = '" + usuario.getIdusuario() + "' ";
				}
				
				if (tipopago!=null) {
					sql += "		and '" + tipopago.getIdtipopago() + "' in (select pago.idtipopago from pago where pago.idcabecera = c.idcabecera ) ";	
				}
				
				if(fabricante!=null) {
					sql += "		and p.idfabricante = '" + fabricante.getIdfabricante() + "' ";
				}
				
				if(grupocategoriaSelected != null) {
					sql += "		and gc.idgrupocategoria = '" + grupocategoriaSelected.getIdgrupocategoria() + "' ";
					
				}
				
				if(categoria!=null) {
					sql += "		and p.idcategoria = '" + categoria.getIdcategoria() + "' ";
					
				}
				if(criterio!=null && !criterio.trim().isEmpty()) {
					
					sql += "		and (UPPER(cl.razonsocial) like '%" + criterio.toUpperCase() + "%' or c.numdocumento like '%" + criterio.toUpperCase() + "%'  or p.codigoprincipal = '" +  criterio + "' or UPPER(p.nombregenerico) like '%" + criterio.toUpperCase() + "%') ";
				}
				
				sql +=	"	order by " +
						"		c.fechaemision, " +
						"		c.secuencial,  " +
						"		d.descripcion ";
			
			
			return resultList(sql, VentasQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param idEstablecimiento
	 * @param criterio
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getFacturas(String idEstablecimiento, String criterio, Date desde, Date hasta)
			throws DaoException {
		try {
			
			return new ArrayList<>();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param idEstablecimiento
	 * @param criterio
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getFacturasAnuladas(String idEstablecimiento, String criterio, Date desde, Date hasta)
			throws DaoException {
		try {
			
			return new ArrayList<>();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param idEstablecimiento
	 * @param criterio
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getRecibos(String idEstablecimiento, String criterio, Date desde, Date hasta)
			throws DaoException {
		try {
			return new ArrayList<>();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @param idEstablecimiento
	 * @param criterio
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getReciboAnulado(String idEstablecimiento, String criterio, Date desde, Date hasta)
			throws DaoException {
		try {
			return new ArrayList<>();
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
	 * @param razonSocial
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getVentasProductoByNombreCliente(String idEstablecimiento,String idEmpresa,Date desde,Date hasta,String razonSocial)throws DaoException{
		try {
			String sql = "select " + 
			"		d.idproducto, " +
			"		p.codigoprincipal, " +
			"		p.nombregenerico, " +
			"		SUM(d.cantidad ) as cantidad, " +
			"		SUM(d.preciototalsinimpuesto ) as preciototalsinimpuesto " +
			"	from  " +
			"		cabecera c inner join cliente cl on c.idcliente = cl.idcliente " +
			"		inner join establecimiento est on est.idestablecimiento = c.idestablecimiento " +
			"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
			"		inner join detalle d on d.idcabecera = c.idcabecera " +
			"		inner join producto p on d.idproducto = p.idproducto  " +
			"	where  " +
			( idEstablecimiento!=null?"		c.idestablecimiento  = '" + idEstablecimiento + "' ":
				"		est.idempresa  = '" + idEmpresa + "' ") +
			"		and tc.identificador in ('00','01') " +
			"		and upper(cl.razonsocial) like '%" + razonSocial.toUpperCase() + "%' " +
			"		and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
			"		and c.estado not in ('ANULADO','BORRADOR') " +
			"	group by " +
			"		d.idproducto, " +
			"		p.codigoprincipal, " +
			"		p.nombregenerico " +
			"	order by  " +
			"		SUM(d.cantidad ) desc ";
			
			return resultList(sql, VentasQuery.class, false);
			
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
	public List<VentasQuery> getVentasProductos(String idEstablecimiento,Date desde,Date hasta,Producto producto)throws DaoException{
		try {
			
			String sql = "select " + 
					"		d.idproducto, " +
					"		p.codigoprincipal, " +
					"		p.nombregenerico, " +
					"		SUM(d.cantidad ) as cantidad, " +
					"		SUM(d.preciototalsinimpuesto ) as preciototalsinimpuesto " +
					"	from  " +
					"		cabecera c inner join cliente cl on c.idcliente = cl.idcliente " + 
					"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
					"		inner join detalle d on d.idcabecera = c.idcabecera " +
					"		inner join producto p on d.idproducto = p.idproducto  " +
					"	where  " +
					"		c.idestablecimiento = '" + idEstablecimiento + "' " +
					"		and tc.identificador in ('00','01') " +
					"		and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
					"		and c.estado not in ('ANULADO','BORRADOR') " +
					(producto!=null? "		and d.idproducto = '" + producto.getIdproducto() + "'":" ") +
					"	group by " +
					"		d.idproducto, " +
					"		p.codigoprincipal, " +
					"		p.nombregenerico " +
					"	order by  " +
					"		SUM(d.cantidad ) desc ";
					
					return resultList(sql, VentasQuery.class, false);
					
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param desde
	 * @param hasta
	 * @param producto
	 * @param idEstablecimiento
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getVentasProductos(Date desde,Date hasta,Producto producto,String idEstablecimiento,String idEmpresa)throws DaoException{
		try {
			
			String sql = "select " + 
					"		d.idproducto, " +
					"		p.codigoprincipal, " +
					"		p.nombregenerico, " +
					"		SUM(d.cantidad ) as cantidad, " +
					"		SUM(d.preciototalsinimpuesto ) as preciototalsinimpuesto " +
					"	from  " +
					"		cabecera c inner join cliente cl on c.idcliente = cl.idcliente " +
					"		inner join establecimiento est on est.idestablecimiento = c.idestablecimiento " +
					"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
					"		inner join detalle d on d.idcabecera = c.idcabecera " +
					"		inner join producto p on d.idproducto = p.idproducto  " +
					"	where  " +
					( idEstablecimiento!=null?"		c.idestablecimiento  = '" + idEstablecimiento + "' ":
						"		est.idempresa  = '" + idEmpresa + "' ") +
					"		and tc.identificador in ('00','01') " +
					"		and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
					"		and c.estado not in ('ANULADO','BORRADOR') " +
					(producto!=null? "		and d.idproducto = '" + producto.getIdproducto() + "'":" ") +
					"	group by " +
					"		d.idproducto, " +
					"		p.codigoprincipal, " +
					"		p.nombregenerico " +
					"	order by  " +
					"		SUM(d.cantidad ) desc ";
					
					return resultList(sql, VentasQuery.class, false);
					
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public List<VentasQuery> getTotalByFecha(String idEstablecimiento,Date fecha)throws DaoException{
		try {
			
			String sql = "select " +
			"		tc.identificador, " +
			"       count(c.idcabecera) as contador, " +
			"		SUM(c.totalsinimpuestos ) as totalsinimpuestos, " +
			"		SUM(c.totalice + c.totaliva ) as impuestos, " +
			"		SUM(c.totalconimpuestos ) as totalconimpuestos  " +
			"	from  " +
			"		cabecera c inner join cliente cl on c.idcliente = cl.idcliente " + 
			"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
			"	where  " +
			"		c.idestablecimiento = '" + idEstablecimiento + "' " +
			"		and tc.identificador in ('01') " +
			"		and c.fechaemision = '" + formatoFecha(fecha) + "'  " +
			"		and c.estado not in ('ANULADO') " +
			"	group by " +
			"		tc.identificador " +
			"	union " +
			"	select  " +
			"		tc.identificador, " +
			"       count(c.idcabecera) as contador, " +
			"		SUM(c.totalsinimpuestos ) as totalsinimpuestos, " +
			"		SUM(c.totalice + c.totaliva ) as impuestos, " +
			"		SUM(c.totalconimpuestos ) as totalconimpuestos " +
			"	from  " +
			"		cabecera c inner join cliente cl on c.idcliente = cl.idcliente " + 
			"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
			"	where  " +
			"		c.idestablecimiento = '" + idEstablecimiento + "' " +
			"		and tc.identificador in ('00') " +
			"		and c.fechaemision = '" + formatoFecha(fecha) + "'  " +
			"		and c.estado not in ('ANULADO') " +
			"	group by " +
			"		tc.identificador ";
			
			return resultList(sql, VentasQuery.class, false);
			
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
	public BigDecimal getConsumoPromedio(String idEstablecimiento,Date desde,Date hasta)throws DaoException{
		try {
			
			String sql = "select " +
				"	AVG(c.totalconimpuestos ) as totalconimpuestos " + 
				"	from  " +
				"		cabecera c inner join cliente cl on c.idcliente = cl.idcliente " + 
				"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
				"		inner join detalle d on d.idcabecera = c.idcabecera " +
//				"		inner join producto p on d.idproducto = p.idproducto  " +
				"	where  " +
				"		c.idestablecimiento = '" + idEstablecimiento + "' " +
				"		and tc.identificador in ('01','00') " +
				"		and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
				"		and c.estado not in ('ANULADO','BORRADOR') ";
			
			Map<String, Object> r = singleResultMap(sql);
			
			if(r!=null && r.get("totalconimpuestos")!=null) {
				return (BigDecimal)r.get("totalconimpuestos");
			}
			
			return BigDecimal.ZERO;
			
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
	public Integer getClientesContador(String idEstablecimiento,Date desde,Date hasta)throws DaoException{
		try {
			
			String sql = "select " +
			"		count(distinct c.idcliente) as contador " + 
			"		from  " +
			"			cabecera c inner join cliente cl on c.idcliente = cl.idcliente " + 
			"			inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
			"			inner join detalle d on d.idcabecera = c.idcabecera " +
//			"			inner join producto p on d.idproducto = p.idproducto  " +
			"		where  " +
			"		c.idestablecimiento = '" + idEstablecimiento + "' " +
			"			and tc.identificador in ('01','00') " +
			"		and c.fechaemision between '" + formatoFecha(desde) + "' and '" + formatoFecha(hasta) + "' " +
			"			and c.estado not in ('ANULADO','BORRADOR') ";
			
			Map<String, Object> r = singleResultMap(sql);
			
			if(r!=null && r.get("contador")!=null) {
				return ((Long)r.get("contador")).intValue();
			}
			
			return 0;
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param fecha
	 * @return
	 * @throws DaoException
	 */
	public List<VentasQuery> getVentasResumenByFecha(String idEstablecimiento,Date fecha)throws DaoException{
		try {
			
			String sql = "select " + 
			"		c.idcabecera, " +
			"		cl.identificacion as identificador, " +
			"		c.numdocumento, " +
			"		c.idcliente, " +
			"		cl.razonsocial, " +
			"		c.totalsinimpuestos, " +
			"		c.totaldescuento, " +
			"		c.totalice, " +
			"		c.totaliva, " +
			"		c.totalconimpuestos, " +
			"		c.updated, " +
			"		c.estado " +
			"	from  " +
			"		cabecera c inner join cliente cl on c.idcliente = cl.idcliente " + 
			"		inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
			"	where  " +
			"		c.idestablecimiento = '" + idEstablecimiento + "' " +
			"		and tc.identificador in ('01') " +
			"		and c.fechaemision = '" + formatoFecha(fecha) + "' " +
			"		and c.estado not in ('ANULADO') " +
			"	order by " +
			"		c.fechaemision, " +
			"		c.updated ";
			
			return resultList(sql, VentasQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
