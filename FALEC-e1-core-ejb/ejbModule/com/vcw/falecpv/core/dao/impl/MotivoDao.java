/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class MotivoDao extends AppGenericDao<Motivo, String> {

	/**
	 * @param type
	 */
	public MotivoDao() {
		super(Motivo.class);
	}

}
