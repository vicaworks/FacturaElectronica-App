/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Transaccion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransaccionDao extends AppGenericDao<Transaccion, String> {

	/**
	 * @param type
	 */
	public TransaccionDao() {
		super(Transaccion.class);
	}

}
