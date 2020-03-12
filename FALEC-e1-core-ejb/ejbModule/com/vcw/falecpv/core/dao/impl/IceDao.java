/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Ice;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IceDao extends AppGenericDao<Ice, String> {

	/**
	 * @param type
	 */
	public IceDao() {
		super(Ice.class);
	}

}
