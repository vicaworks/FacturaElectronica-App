/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Iva;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IvaDao extends AppGenericDao<Iva, String> {

	/**
	 * @param type
	 */
	public IvaDao() {
		super(Iva.class);
	}

}
