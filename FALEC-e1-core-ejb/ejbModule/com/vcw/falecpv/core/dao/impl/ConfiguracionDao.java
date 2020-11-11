/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import javax.ejb.Stateless;

import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Configuracion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ConfiguracionDao extends AppGenericDao<Configuracion, String> {

	/**
	 * @param type
	 */
	public ConfiguracionDao() {
		super(Configuracion.class);
	}

}
