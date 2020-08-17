/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Transacciontipo;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransacciontipoDao extends AppGenericDao<Transacciontipo, String> {

	/**
	 * @param type
	 */
	public TransacciontipoDao() {
		super(Transacciontipo.class);
	}

}
