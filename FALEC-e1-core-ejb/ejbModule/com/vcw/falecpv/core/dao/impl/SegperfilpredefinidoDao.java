/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinido;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilpredefinidoDao extends AppGenericDao<Segperfilpredefinido, String> {

	/**
	 * @param type
	 */
	public SegperfilpredefinidoDao() {
		super(Segperfilpredefinido.class);
	}

}
