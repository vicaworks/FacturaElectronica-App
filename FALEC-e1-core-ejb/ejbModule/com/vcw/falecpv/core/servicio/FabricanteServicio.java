/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.FabricanteDao;
import com.vcw.falecpv.core.modelo.persistencia.Fabricante;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class FabricanteServicio extends AppGenericService<Fabricante, String> {

	@Inject
	private FabricanteDao fabricanteDao;
	
	/**
	 * 
	 */
	public FabricanteServicio() {
	}

	@Override
	public List<Fabricante> consultarActivos() {
		return null;
	}

	@Override
	public List<Fabricante> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Fabricante, String> getDao() {
		return fabricanteDao;
	}

}
