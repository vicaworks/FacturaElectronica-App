/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Errorsri;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ErrorsriDao extends AppGenericDao<Errorsri, String> {

	/**
	 * @param type
	 */
	public ErrorsriDao() {
		super(Errorsri.class);
	}

}
