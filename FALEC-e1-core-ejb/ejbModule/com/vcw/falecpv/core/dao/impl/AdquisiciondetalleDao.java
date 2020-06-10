/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisiciondetalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class AdquisiciondetalleDao extends AppGenericDao<Adquisiciondetalle, String> {

	/**
	 * @param type
	 */
	public AdquisiciondetalleDao() {
		super(Adquisiciondetalle.class);
	}

}
