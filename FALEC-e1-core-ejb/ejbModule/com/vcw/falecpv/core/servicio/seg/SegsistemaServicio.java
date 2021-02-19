/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegsistemaDao;
import com.vcw.falecpv.core.modelo.persistencia.Segsistema;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegsistemaServicio extends AppGenericService<Segsistema, String> {

	@Inject
	private SegsistemaDao segsistemaDao;
	
	@Override
	public List<Segsistema> consultarActivos() {
		return null;
	}

	@Override
	public List<Segsistema> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segsistema, String> getDao() {
		return segsistemaDao;
	}

	/**
	 * @return the segsistemaDao
	 */
	public SegsistemaDao getSegsistemaDao() {
		return segsistemaDao;
	}


}
