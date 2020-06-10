/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DestinatarioDao extends AppGenericDao<Destinatario, String> {

	/**
	 * @param type
	 */
	public DestinatarioDao() {
		super(Destinatario.class);
	}

}
