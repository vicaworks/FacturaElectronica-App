/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Transaccionconcepto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransaccionconceptoDao extends AppGenericDao<Transaccionconcepto, String> {

	/**
	 * @param type
	 */
	public TransaccionconceptoDao() {
		super(Transaccionconcepto.class);
	}

}
