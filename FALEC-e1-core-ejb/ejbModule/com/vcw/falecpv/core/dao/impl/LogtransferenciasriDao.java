/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Logtransferenciasri;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class LogtransferenciasriDao extends AppGenericDao<Logtransferenciasri, String> {

	/**
	 * @param type
	 */
	public LogtransferenciasriDao() {
		super(Logtransferenciasri.class);
	}

}
