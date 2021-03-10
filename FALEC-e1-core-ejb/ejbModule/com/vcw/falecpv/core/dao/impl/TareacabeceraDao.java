/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TareacabeceraDao extends AppGenericDao<Tareacabecera, String> {

	/**
	 * @param type
	 */
	public TareacabeceraDao() {
		super(Tareacabecera.class);
	}

}
