/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Pagodetalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class PagodetalleDao extends AppGenericDao<Pagodetalle, String> {

	/**
	 * @param type
	 */
	public PagodetalleDao() {
		super(Pagodetalle.class);
	}

}
