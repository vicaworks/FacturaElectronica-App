/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegperfilopcionDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilopcion;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilopcionServicio extends AppGenericService<Segperfilopcion, String> {

	@Inject
	private SegperfilopcionDao segperfilopcionDao;
	
	
	/**
	 * 
	 */
	public SegperfilopcionServicio() {
	}

	@Override
	public List<Segperfilopcion> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfilopcion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfilopcion, String> getDao() {
		return segperfilopcionDao;
	}

	/**
	 * @return the segperfilopcionDao
	 */
	public SegperfilopcionDao getSegperfilopcionDao() {
		return segperfilopcionDao;
	}

}
