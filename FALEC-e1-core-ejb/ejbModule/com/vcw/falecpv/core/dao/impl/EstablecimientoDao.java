/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EstablecimientoDao extends AppGenericDao<Establecimiento, String> {

	/**
	 * @param type
	 */
	public EstablecimientoDao() {
		super(Establecimiento.class);
	}

}
