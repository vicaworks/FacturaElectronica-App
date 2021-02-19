/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segtipoopcion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegtipoopcionDao extends AppGenericDao<Segtipoopcion, String> {

	/**
	 * @param type
	 */
	public SegtipoopcionDao() {
		super(Segtipoopcion.class);
	}

}
