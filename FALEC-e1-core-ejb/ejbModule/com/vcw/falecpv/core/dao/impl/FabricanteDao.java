/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class FabricanteDao extends AppGenericDao<Fabricante, String> {

	/**
	 * @param type
	 */
	public FabricanteDao() {
		super(Fabricante.class);
	}

}
