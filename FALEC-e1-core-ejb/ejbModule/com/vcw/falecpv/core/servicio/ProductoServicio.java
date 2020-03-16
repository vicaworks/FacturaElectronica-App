/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.ProductoDao;
import com.vcw.falecpv.core.modelo.persistencia.Producto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ProductoServicio extends AppGenericService<Producto, String> {

	@Inject
	private ProductoDao productoDao;
	
	/**
	 * 
	 */
	public ProductoServicio() {
	}

	@Override
	public List<Producto> consultarActivos() {
		return null;
	}

	@Override
	public List<Producto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Producto, String> getDao() {
		return productoDao;
	}

	/**
	 * @return the productoDao
	 */
	public ProductoDao getProductoDao() {
		return productoDao;
	}

}
