/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.RetencionDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionServicio extends AppGenericService<Retencion, String> {

	@Inject
	private RetencionDao retencionDao;
	

	@Override
	public List<Retencion> consultarActivos() {
		return null;
	}

	@Override
	public List<Retencion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Retencion, String> getDao() {
		return retencionDao;
	}

	/**
	 * @return the retencionDao
	 */
	public RetencionDao getRetencionDao() {
		return retencionDao;
	}

}
