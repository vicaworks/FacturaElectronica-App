/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.ErrorsriDao;
import com.vcw.falecpv.core.modelo.persistencia.Errorsri;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ErrorsriServicio extends AppGenericService<Errorsri, String> {
	
	@Inject
	private ErrorsriDao errorsriDao;

	/**
	 * 
	 */
	public ErrorsriServicio() {
	}

	@Override
	public List<Errorsri> consultarActivos() {
		return null;
	}

	@Override
	public List<Errorsri> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Errorsri, String> getDao() {
		return errorsriDao;
	}

	/**
	 * @return the errorsriDao
	 */
	public ErrorsriDao getErrorsriDao() {
		return errorsriDao;
	}

}
