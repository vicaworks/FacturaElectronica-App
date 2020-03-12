/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.EstablecimientoDao;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class EstablecimientoServicio extends AppGenericService<Establecimiento, String> {
	
	@Inject
	private EstablecimientoDao establecimientoDao;
	
	/**
	 * 
	 */
	public EstablecimientoServicio() {
	}

	@Override
	public List<Establecimiento> consultarActivos() {
		return null;
	}

	@Override
	public List<Establecimiento> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Establecimiento, String> getDao() {
		return establecimientoDao;
	}

}
