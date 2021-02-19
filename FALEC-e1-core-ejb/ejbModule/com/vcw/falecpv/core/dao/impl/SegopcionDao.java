/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Segopcion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegopcionDao extends AppGenericDao<Segopcion, String> {

	/**
	 * @param type
	 */
	public SegopcionDao() {
		super(Segopcion.class);
	}

}
