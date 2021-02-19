/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegtipoopcionDao;
import com.vcw.falecpv.core.modelo.persistencia.Segtipoopcion;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegtipoopcionServicio extends AppGenericService<Segtipoopcion, String> {

	@Inject
	private SegtipoopcionDao segtipoopcionDao;
	
	/**
	 * 
	 */
	public SegtipoopcionServicio() {
	}

	@Override
	public List<Segtipoopcion> consultarActivos() {
		return null;
	}

	@Override
	public List<Segtipoopcion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segtipoopcion, String> getDao() {
		return segtipoopcionDao;
	}

	/**
	 * @return the segtipoopcionDao
	 */
	public SegtipoopcionDao getSegtipoopcionDao() {
		return segtipoopcionDao;
	}

}
