/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tareaetiqueta;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TareaetiquetaDao extends AppGenericDao<Tareaetiqueta, String> {

	/**
	 * @param type
	 */
	public TareaetiquetaDao() {
		super(Tareaetiqueta.class);
	}

}
