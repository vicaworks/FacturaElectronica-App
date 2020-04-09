/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class AdquisicionDao extends AppGenericDao<Adquisicion, String> {

	/**
	 * @param type
	 */
	public AdquisicionDao() {
		super(Adquisicion.class);
	}

}
