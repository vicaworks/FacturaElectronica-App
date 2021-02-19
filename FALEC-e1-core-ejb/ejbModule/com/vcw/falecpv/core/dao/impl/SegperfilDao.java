/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilDao extends AppGenericDao<Segperfil, String> {

	/**
	 * @param type
	 */
	public SegperfilDao() {
		super(Segperfil.class);
	}

}
