/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segsistema;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegsistemaDao extends AppGenericDao<Segsistema, String> {

	/**
	 * @param type
	 */
	public SegsistemaDao() {
		super(Segsistema.class);
	}

}
