/**
 * 
 */
package com.vcw.falecpv.core.servicio.query;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGVentasPopUpEnum;
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.modelo.query.ResumenCabeceraQuery;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.util.SqlUtil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ConsultaGeneralComprobanteServicio extends DBUtilGenericoApp {
	
	@EJB
	private ParametroGenericoServicio parametroGenericoServicio;

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
			Date desde, Date hasta,String tipoComprobante) throws DaoException {
		try {
			
			// consulta de parametrosgenericos los tipos de comprobantes incluidos
			String identificadorComprobante = null;
			switch (tipoComprobante) {
			case "GUIA_REMISION":
				identificadorComprobante = (String)parametroGenericoServicio.consultarParametro(PGVentasPopUpEnum.GUIA_REMISION, TipoRetornoParametroGenerico.STRING);
				break;
			case "NOTA_DEBITO":
				identificadorComprobante = (String)parametroGenericoServicio.consultarParametro(PGVentasPopUpEnum.NOTA_DEBITO, TipoRetornoParametroGenerico.STRING);
				break;	
			default:
				break;
			}
			
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
							"		c.estado,	 " +
							"		c.claveacceso, " +
							"		c.estadoautorizacion, " +
							"		c.numeroautorizacion, " +
							"		c.totalbaseimponible, " +
							"		c.totalsinimpuestos, " +
							"		c.totalice, " +
							"		c.totaliva, " +
							"		c.totalconimpuestos, " +
							"		c.idguiaremision " +
							"	from  " +
							"		cabecera c inner join tipocomprobante tc on tc.idtipocomprobante = c.idtipocomprobante " + 
							"		left join cliente cl on cl.idcliente = c.idcliente " + 
							"	where " +
							"		c.idestablecimiento = '" + idEstablecimiento + "' " +
							"		and c.estado not in ('ANULADO','BORRADOR') " +
							"		and tc.identificador in (" + identificadorComprobante + ")";
//							"		and tc.identificador in ('00','01')";
			
			if(criteria!=null && !criteria.trim().isEmpty()) {
				sql += "   and (c.secuencial like '%" + criteria.toUpperCase()  + "%' "
						+ " or c.numdocumento like '%" + criteria.toUpperCase()  + "%' "
						+ " or UPPER(cl.razonsocial) like '%" + criteria.toUpperCase()  + "%' "
						+ " or cl.identificacion like'%" + criteria.toUpperCase()  + "%') ";
			}else {
				sql += " and c.fechaemision between '" + SqlUtil.formatPostgresDate(desde) + "' and '" + SqlUtil.formatPostgresDate(hasta) + "' ";
			}
			
			sql += " order by " +
					" 	c.fechaemision DESC, " +
					" cl.razonsocial ";
			
			return resultList(sql, ResumenCabeceraQuery.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
