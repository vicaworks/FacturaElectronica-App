/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipoconfiguracion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipoconfiguracionDao extends AppGenericDao<Tipoconfiguracion, String> {

	/**
	 * @param type
	 */
	public TipoconfiguracionDao() {
		super(Tipoconfiguracion.class);
	}

}
