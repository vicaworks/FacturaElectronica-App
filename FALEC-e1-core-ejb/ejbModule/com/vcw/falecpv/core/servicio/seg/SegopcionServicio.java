/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegopcionDao;
import com.vcw.falecpv.core.modelo.persistencia.Segopcion;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegopcionServicio extends AppGenericService<Segopcion, String> {

	@Inject
	private SegopcionDao segopcionDao;
	
	/**
	 * 
	 */
	public SegopcionServicio() {
	}

	@Override
	public List<Segopcion> consultarActivos() {
		return null;
	}

	@Override
	public List<Segopcion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segopcion, String> getDao() {
		return segopcionDao;
	}

	/**
	 * @return the segopcionDao
	 */
	public SegopcionDao getSegopcionDao() {
		return segopcionDao;
	}

}
