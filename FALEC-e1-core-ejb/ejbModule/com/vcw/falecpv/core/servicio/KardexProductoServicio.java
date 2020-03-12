/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.KardexProductoDao;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class KardexProductoServicio extends AppGenericService<KardexProducto, String> {
	
	@Inject
	private KardexProductoDao kardexProductoDao;

	/**
	 * 
	 */
	public KardexProductoServicio() {
	}

	@Override
	public List<KardexProducto> consultarActivos() {
		return null;
	}

	@Override
	public List<KardexProducto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<KardexProducto, String> getDao() {
		return kardexProductoDao;
	}

}
