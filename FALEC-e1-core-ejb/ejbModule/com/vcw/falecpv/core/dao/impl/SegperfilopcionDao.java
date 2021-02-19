/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilopcion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilopcionDao extends AppGenericDao<Segperfilopcion, String> {

	/**
	 * @param type
	 */
	public SegperfilopcionDao() {
		super(Segperfilopcion.class);
	}

}
