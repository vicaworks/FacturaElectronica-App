/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipopagoDao extends AppGenericDao<Tipopago, String> {

	/**
	 * @param type
	 */
	public TipopagoDao() {
		super(Tipopago.class);
	}

}
