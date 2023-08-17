/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.dao.impl.GrupocategoriaDao;

/**
 * 
 */
@Stateless
public class GrupocategoriaServicio extends DBUtilGenericoApp {

	@Inject
	private GrupocategoriaDao grupocategoriaDao;

	/**
	 * @return the grupocategoriaDao
	 */
	public GrupocategoriaDao getGrupocategoriaDao() {
		return grupocategoriaDao;
	}

}
