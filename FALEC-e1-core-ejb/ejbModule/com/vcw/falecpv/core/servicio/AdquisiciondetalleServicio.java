/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.AdquisiciondetalleDao;
import com.vcw.falecpv.core.modelo.persistencia.Adquisiciondetalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class AdquisiciondetalleServicio extends AppGenericService<Adquisiciondetalle, String> {
	
	@Inject
	private AdquisiciondetalleDao adquisiciondetalleDao;

	@Override
	public List<Adquisiciondetalle> consultarActivos() {
		return null;
	}

	@Override
	public List<Adquisiciondetalle> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Adquisiciondetalle, String> getDao() {
		return null;
	}

	/**
	 * @return the adquisiciondetalleDao
	 */
	public AdquisiciondetalleDao getAdquisiciondetalleDao() {
		return adquisiciondetalleDao;
	}

}
