/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.servitec.common.dao.exception.DaoException;
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
	public List<VentasQuery> getVentasTotales(Usuario usuario, Tipopago tipopago, Fabricante fabricante,
			Categoria categoria, String idEstablecimiento, String criterio, Date desde, Date hasta)
			throws DaoException {
		try {
			
			String sql = "SELECT c FROM cabecera c WHERE c.fechaemision = '" + SqlUtil.formatPostgresDate(desde) + "'";
			
			System.out.println(sql);
			
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
