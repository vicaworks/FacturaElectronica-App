/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Pago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class PagoDao extends AppGenericDao<Pago, String> {

	/**
	 * @param type
	 */
	public PagoDao() {
		super(Pago.class);
	}

}
