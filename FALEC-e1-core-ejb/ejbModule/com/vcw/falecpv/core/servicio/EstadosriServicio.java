/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.EstadosriDao;
import com.vcw.falecpv.core.modelo.persistencia.Estadosri;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EstadosriServicio extends AppGenericService<Estadosri, String> {

	@Inject
	private EstadosriDao estadosriDao;
	
	/**
	 * 
	 */
	public EstadosriServicio() {
	}

	@Override
	public List<Estadosri> consultarActivos() {
		return null;
	}

	@Override
	public List<Estadosri> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Estadosri, String> getDao() {
		return estadosriDao;
	}

	/**
	 * @return the estadosriDao
	 */
	public EstadosriDao getEstadosriDao() {
		return estadosriDao;
	}

}
