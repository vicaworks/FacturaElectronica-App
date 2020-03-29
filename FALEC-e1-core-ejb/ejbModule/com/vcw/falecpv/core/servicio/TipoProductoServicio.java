/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.TipoProductoDao;
import com.vcw.falecpv.core.modelo.persistencia.TipoProducto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipoProductoServicio extends AppGenericService<TipoProducto, String> {

	@Inject
	private TipoProductoDao tipoProductoDao;
	
	/**
	 * 
	 */
	public TipoProductoServicio() {
	}

	@Override
	public List<TipoProducto> consultarActivos() {
		return null;
	}

	@Override
	public List<TipoProducto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<TipoProducto, String> getDao() {
		return tipoProductoDao;
	}

	/**
	 * @return the tipoProductoDao
	 */
	public TipoProductoDao getTipoProductoDao() {
		return tipoProductoDao;
	}

}
