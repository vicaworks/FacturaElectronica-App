/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Etiqueta;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EtiquetaDao extends AppGenericDao<Etiqueta, String> {

	/**
	 * @param type
	 */
	public EtiquetaDao() {
		super(Etiqueta.class);
	}

}
