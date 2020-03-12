/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Producto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ProductoDao extends AppGenericDao<Producto, String> {

	/**
	 * @param type
	 */
	public ProductoDao() {
		super(Producto.class);
	}

}
