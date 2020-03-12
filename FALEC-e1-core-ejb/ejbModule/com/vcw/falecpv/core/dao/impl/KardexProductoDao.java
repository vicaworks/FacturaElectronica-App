/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.KardexProducto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class KardexProductoDao extends AppGenericDao<KardexProducto, String> {

	/**
	 * @param type
	 */
	public KardexProductoDao() {
		super(KardexProducto.class);
	}

}
