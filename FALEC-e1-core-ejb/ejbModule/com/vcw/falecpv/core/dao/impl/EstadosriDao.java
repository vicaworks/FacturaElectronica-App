/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Estadosri;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EstadosriDao extends AppGenericDao<Estadosri, String> {

	/**
	 * @param type
	 */
	public EstadosriDao() {
		super(Estadosri.class);
	}

}
