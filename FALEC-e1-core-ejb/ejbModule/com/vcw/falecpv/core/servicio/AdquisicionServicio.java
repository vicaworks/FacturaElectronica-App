/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.AdquisicionDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class AdquisicionServicio extends AppGenericService<Adquisicion, String> {
	
	@Inject
	private AdquisicionDao adquisicionDao;

	@Override
	public List<Adquisicion> consultarActivos() {
		return null;
	}

	@Override
	public List<Adquisicion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Adquisicion, String> getDao() {
		return null;
	}

	/**
	 * @return the adquisicionDao
	 */
	public AdquisicionDao getAdquisicionDao() {
		return adquisicionDao;
	}

}
