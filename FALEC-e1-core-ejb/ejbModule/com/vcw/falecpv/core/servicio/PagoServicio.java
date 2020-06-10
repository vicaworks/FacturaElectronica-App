/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.PagoDao;
import com.vcw.falecpv.core.modelo.persistencia.Pago;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class PagoServicio extends AppGenericService<Pago, String> {

	@Inject
	private PagoDao pagoDao;
	
	/**
	 * 
	 */
	public PagoServicio() {
	}

	@Override
	public List<Pago> consultarActivos() {
		return null;
	}

	@Override
	public List<Pago> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Pago, String> getDao() {
		return pagoDao;
	}

	/**
	 * @return the pagoDao
	 */
	public PagoDao getPagoDao() {
		return pagoDao;
	}

}
