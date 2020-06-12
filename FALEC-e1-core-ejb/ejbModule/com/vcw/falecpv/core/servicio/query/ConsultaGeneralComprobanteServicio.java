/**
 * 
 */
package com.vcw.falecpv.core.servicio.query;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.modelo.query.ResumenCabeceraQuery;
import com.vcw.falecpv.core.util.SqlUtil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ConsultaGeneralComprobanteServicio extends DBUtilGenericoApp {

	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param criteria
	 * @param desde
	 * @param hasta
	 * @param tipocomprobanteList
	 * @return
	 * @throws DaoException
	 */
	public List<ResumenCabeceraQuery> getComprovantesVentasByDateCriteria(String idEstablecimiento, String criteria,
			Date desde, Date hasta) throws DaoException {
		try {
			
			
			String sql = "select " +
							"		c.fechaemision, " +
							"		c.idcabecera, " +
							"		c.idtipocomprobante, " +
							"		tc.identificador as indentificadorcomprobante, " +
							"		tc.comprobante, " +
							"		c.secuencial, " +
							"		c.numdocumento, " +
							"		c.numfactura, " +
							"		c.idcliente, " +
							"		cl.identificacion as clienteidentificacion, " +
							"		cl.razonsocial as cliente, " +
							"		c.idproveedor, " +
							"		p.identificacion as proveedoridentificacion, " +
							"		p.razonsocial as proveedor, " +
							"		c.estado,	 " +
							"		c.claveacceso, " +
							"		c.estadoautorizacion, " +
							"		c.numeroautorizacion, " +
							"		c.totalbaseimponible, " +
							"		c.totalsinimpuestos, " +
							"		c.totalice, " +
							"		c.totaliva, " +
							"		c.totalconimpuestos " +
							"	from  " +
							"		cabecera c inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
							"		left join cliente cl on cl.idcliente = c.idcliente " + 
							"		left join proveedor p on p.idproveedor = c.idproveedor " + 
							"	where " +
							"		c.idestablecimiento = '" + idEstablecimiento + "' " +
							"		and c.estado <> 'ANULADO' " +
							"		and tc.identificador in ('00','01')";
			
			if(criteria!=null && !criteria.trim().isEmpty()) {
				sql += "   and (c.secuencial like '%" + criteria.toUpperCase()  + "%' "
						+ " or c.numdocumento like '%" + criteria.toUpperCase()  + "%' "
						+ " or UPPER(cl.razonsocial) like '%" + criteria.toUpperCase()  + "%' "
						+ " or cl.identificacion like'%" + criteria.toUpperCase()  + "%' "
						+ " or UPPER(p.razonsocial) like '%" + criteria.toUpperCase()  + "%' "
						+ " or p.identificacion like '%" + criteria.toUpperCase()  + "%') ";
			}else {
				sql += " and c.fechaemision between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' ";
			}
			
			sql += " order by " +
					" 	c.fechaemision DESC, " +
					" cl.razonsocial, " +
					" p.razonsocial ";
			
			return resultList(sql, ResumenCabeceraQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
