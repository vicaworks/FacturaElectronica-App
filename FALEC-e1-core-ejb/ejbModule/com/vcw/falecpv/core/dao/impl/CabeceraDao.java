/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CabeceraDao extends AppGenericDao<Cabecera, String> {

	/**
	 * @param type
	 */
	public CabeceraDao() {
		super(Cabecera.class);
	}

}
