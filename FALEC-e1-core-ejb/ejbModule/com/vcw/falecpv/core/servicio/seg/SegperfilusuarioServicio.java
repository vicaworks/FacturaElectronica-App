/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegperfilusuarioDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilusuario;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilusuarioServicio extends AppGenericService<Segperfilusuario, String> {

	@Inject
	private SegperfilusuarioDao segperfilusuarioDao;
	
	
	/**
	 * 
	 */
	public SegperfilusuarioServicio() {
	}

	@Override
	public List<Segperfilusuario> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfilusuario> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfilusuario, String> getDao() {
		return segperfilusuarioDao;
	}

	/**
	 * @return the segperfilusuarioDao
	 */
	public SegperfilusuarioDao getSegperfilusuarioDao() {
		return segperfilusuarioDao;
	}

}
