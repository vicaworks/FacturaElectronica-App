/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.TareaetiquetaDao;
import com.vcw.falecpv.core.modelo.persistencia.Tareaetiqueta;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TareaetiquetaServicio extends AppGenericService<Tareaetiqueta, String> {

	@Inject
	private TareaetiquetaDao tareaetiquetaDao;
	
	/**
	 * 
	 */
	public TareaetiquetaServicio() {
	}

	@Override
	public List<Tareaetiqueta> consultarActivos() {
		return null;
	}

	@Override
	public List<Tareaetiqueta> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Tareaetiqueta, String> getDao() {
		return tareaetiquetaDao;
	}

	/**
	 * @return the tareaetiquetaDao
	 */
	public TareaetiquetaDao getTareaetiquetaDao() {
		return tareaetiquetaDao;
	}

}
