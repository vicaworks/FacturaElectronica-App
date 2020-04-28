/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class InfoadicionalDao extends AppGenericDao<Infoadicional, String> {

	/**
	 * @param type
	 */
	public InfoadicionalDao() {
		super(Infoadicional.class);
	}

}
