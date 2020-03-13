/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenerico;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ParametroGenericoDao extends AppGenericDao<ParametroGenerico, String> {

	/**
	 * @param type
	 */
	public ParametroGenericoDao() {
		super(ParametroGenerico.class);
	}

}
