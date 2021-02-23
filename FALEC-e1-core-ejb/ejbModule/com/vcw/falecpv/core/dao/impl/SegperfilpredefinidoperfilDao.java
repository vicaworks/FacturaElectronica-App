/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinidoperfil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilpredefinidoperfilDao extends AppGenericDao<Segperfilpredefinidoperfil, String> {

	/**
	 * @param type
	 */
	public SegperfilpredefinidoperfilDao() {
		super(Segperfilpredefinidoperfil.class);
	}

}
