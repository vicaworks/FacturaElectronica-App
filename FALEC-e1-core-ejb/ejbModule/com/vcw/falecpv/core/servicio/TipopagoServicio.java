/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipopagoServicio extends AppGenericService<Tipopago, String> {

	
	@Inject
	private TipopagoServicio tipopagoDao; 
	
	@Override
	public List<Tipopago> consultarActivos() {
		return null;
	}

	@Override
	public List<Tipopago> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Tipopago, String> getDao() {
		return null;
	}

	/**
	 * @return the tipopagoDao
	 */
	public TipopagoServicio getTipopagoDao() {
		return tipopagoDao;
	}

}
