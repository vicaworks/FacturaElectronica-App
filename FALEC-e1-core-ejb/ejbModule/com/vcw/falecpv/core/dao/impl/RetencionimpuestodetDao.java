/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuestodet;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestodetDao extends AppGenericDao<Retencionimpuestodet, String> {

	/**
	 * @param type
	 */
	public RetencionimpuestodetDao() {
		super(Retencionimpuestodet.class);
	}

}
