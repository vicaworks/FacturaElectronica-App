/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.ParametroGenericoDao;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenerico;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ParametroGenericoServicio extends AppGenericService<ParametroGenerico, String> {

	@Inject
	private ParametroGenericoDao parametroGenericoDao;
	
	/**
	 * 
	 */
	public ParametroGenericoServicio() {
	}

	@Override
	public List<ParametroGenerico> consultarActivos() {
		return null;
	}

	@Override
	public List<ParametroGenerico> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<ParametroGenerico, String> getDao() {
		return parametroGenericoDao;
	}

}
