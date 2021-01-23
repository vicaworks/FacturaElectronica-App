/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.vista.VComprobantesresumen;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class VComprobantesresumenDao extends AppGenericDao<VComprobantesresumen, String> {

	/**
	 * @param type
	 */
	public VComprobantesresumenDao() {
		super(VComprobantesresumen.class);
	}

}
