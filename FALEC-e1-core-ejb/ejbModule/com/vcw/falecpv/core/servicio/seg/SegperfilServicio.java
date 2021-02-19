/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegperfilDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilServicio extends AppGenericService<Segperfil, String> {

	@Inject
	private SegperfilDao segperfilDao;
	
	/**
	 * 
	 */
	public SegperfilServicio() {
	}

	@Override
	public List<Segperfil> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfil> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfil, String> getDao() {
		return segperfilDao;
	}

	/**
	 * @return the segperfilDao
	 */
	public SegperfilDao getSegperfilDao() {
		return segperfilDao;
	}

}
