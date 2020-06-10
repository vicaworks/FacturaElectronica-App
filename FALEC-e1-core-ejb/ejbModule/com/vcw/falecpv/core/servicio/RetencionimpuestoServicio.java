/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.RetencionimpuestoDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestoServicio extends AppGenericService<Retencionimpuesto, String> {
	
	@Inject
	private RetencionimpuestoDao retencionimpuestoDao;

	@Override
	public List<Retencionimpuesto> consultarActivos() {
		return null;
	}

	@Override
	public List<Retencionimpuesto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Retencionimpuesto, String> getDao() {
		return retencionimpuestoDao;
	}

	/**
	 * @return the retencionimpuestoDao
	 */
	public RetencionimpuestoDao getRetencionimpuestoDao() {
		return retencionimpuestoDao;
	}

}
