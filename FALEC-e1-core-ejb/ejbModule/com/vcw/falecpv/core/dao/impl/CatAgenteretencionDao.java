/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.CatAgenteretencion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CatAgenteretencionDao extends AppGenericDao<CatAgenteretencion, String> {

	/**
	 * @param type
	 */
	public CatAgenteretencionDao() {
		super(CatAgenteretencion.class);
	}

}
