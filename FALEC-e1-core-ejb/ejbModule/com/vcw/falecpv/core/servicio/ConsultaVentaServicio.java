/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.modelo.persistencia.Categoria;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;
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
	public List<VentasQuery> getFacturasEmitidas(String idusuario,String criterio,Date desde,Date hasta,String idEstablecimiento,GenTipoDocumentoEnum genTipoDocumentoEnum)throws DaoException{
		try {
			
			String sql = "select " + 
						"	c.idcabecera, " +
						"	c.secuencial, " +
						"	c.fechaemision, " +
						"	c.idcliente, " +
						"	cl.razonsocial, " +
						"	u.nombrepantalla, " +
						"	(select SUM(d.cantidad) from detalle d where d.idcabecera = c.idcabecera ) as cantidad, " +
						"	c.totalsinimpuestos as subtotal, " +
						"	c.totaliva iva, " +
						"	c.totalice ice,  " +
						"   c.totaldescuento, " +
						"	c.totalconimpuestos as total, " +
						"	c.estado, " +
						"	c.estadoautorizacion, " +
						"	(select SUM(p.valorentrega ) from pago p where p.idcabecera = c.idcabecera ) as licitado, " +
						"	(select SUM(p.cambio ) from pago p where p.idcabecera = c.idcabecera ) as cambio, " +
						"   (select SUM(p.total ) from pago p where p.idcabecera = c.idcabecera ) as totalpago " +
					"	from " +
					"		cabecera c inner join cliente cl on cl.idcliente =c.idcliente " + 
					"		inner join tipocomprobante  tc on tc.idtipocomprobante =c.idtipocomprobante " +
					"		inner join usuario u on u.idusuario = c.idusuario  " +
					"	where  " +
					"		c.fechaemision between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' " +
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
			Categoria categoria, String idEstablecimiento, String criterio, Date desde, Date hasta)
			throws DaoException {
		try {
			
			String sql =  "select " +
							"	DISTINCT " +
							"	d.iddetalle, " +
							"	c.idcabecera, " +
							"	c.idtipocomprobante, " +
							"	c.estado, " +
							"	tc.identificador, " + 
							"	c.secuencial, " +
							"	c.fechaemision, " +
							"	c.idcliente, " +
							"	cl.razonsocial, " +
							"	d.idproducto, " +
							"   p.codigoprincipal, " +
							"   f.nombrecomercial as fabricante, " +
							"   ca.descripcion as categoria, " +
							"	d.descripcion, " +
							"	d.cantidad, " +
							"	d.preciounitario, " +
							"	d.descuento, " +
							"	d.preciototalsinimpuesto, " +
							"	d.idiva, " +
							"	iva.valor as porcentajeiva, " +
							"	d.valoriva, " +
							"	d.idice, " +
							"	ice.valor as porcentajeice, " +
							"	d.valorice,	 " +
							"	d.preciototal,  " +
							"   u.nombrepantalla, " +
							"   c.estadoautorizacion " +
						"	from  " +
						"		cabecera c inner join cliente cl on cl.idcliente = c.idcliente " +
						"		inner join tipocomprobante  tc on tc.idtipocomprobante =c.idtipocomprobante " +	 
						"		inner join usuario u on u.idusuario = c.idusuario " +
						"		inner join detalle d on d.idcabecera = c.idcabecera " +
						"		left join producto p on p.idproducto = d.idproducto " +
						"       left join fabricante f on p.idfabricante = f.idfabricante " + 
						"       left join categoria ca on ca.idcategoria = p.idcategoria " +
						"		inner join iva on iva.idiva = d.idiva  " +
						"		inner join ice on ice.idice  = d.idice  " +
						"	where " +
						"		c.fechaemision between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' " +
						"		and c.idestablecimiento  = '" + idEstablecimiento + "' " +
						"		and tc.identificador in ('00','01') " +
						"		and c.estado <> 'ANULADO' ";
						
						
				if(usuario!=null) {
					sql +=	"		and c.idusuario = '" + usuario.getIdusuario() + "' ";
				}
				
				if (tipopago!=null) {
					sql += "		and '" + tipopago.getIdtipopago() + "' in (select pago.idtipopago from pago where pago.idcabecera = c.idcabecera ) ";	
				}
				
				if(fabricante!=null) {
					sql += "		and p.idfabricante = '" + fabricante.getIdfabricante() + "' ";
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
	

}
