/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegtiposistemaDao;
import com.vcw.falecpv.core.modelo.persistencia.Segtiposistema;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegtiposistemaServicio extends AppGenericService<Segtiposistema, String> {

	@Inject
	private SegtiposistemaDao segtiposistemaDao;
	
	/**
	 * 
	 */
	public SegtiposistemaServicio() {
	}

	@Override
	public List<Segtiposistema> consultarActivos() {
		return null;
	}

	@Override
	public List<Segtiposistema> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segtiposistema, String> getDao() {
		return segtiposistemaDao;
	}

	/**
	 * @return the segtiposistemaDao
	 */
	public SegtiposistemaDao getSegtiposistemaDao() {
		return segtiposistemaDao;
	}

}
