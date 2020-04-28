/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TotalimpuestoDao extends AppGenericDao<Totalimpuesto, String> {

	/**
	 * @param type
	 */
	public TotalimpuestoDao() {
		super(Totalimpuesto.class);
	}

}
