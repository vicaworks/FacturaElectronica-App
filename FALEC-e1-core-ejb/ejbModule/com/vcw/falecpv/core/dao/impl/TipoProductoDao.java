/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.TipoProducto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipoProductoDao extends AppGenericDao<TipoProducto, String> {

	/**
	 * @param type
	 */
	public TipoProductoDao() {
		super(TipoProducto.class);
	}

}
