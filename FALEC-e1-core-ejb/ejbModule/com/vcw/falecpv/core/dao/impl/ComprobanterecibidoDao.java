/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ComprobanterecibidoDao extends AppGenericDao<Comprobanterecibido, String> {

	/**
	 * @param type
	 */
	public ComprobanterecibidoDao() {
		super(Comprobanterecibido.class);
	}

}
