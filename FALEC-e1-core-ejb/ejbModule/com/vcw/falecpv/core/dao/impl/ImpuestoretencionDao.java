/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Impuestoretencion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ImpuestoretencionDao extends AppGenericDao<Impuestoretencion, String> {

	/**
	 * @param type
	 */
	public ImpuestoretencionDao() {
		super(Impuestoretencion.class);
	}

}
