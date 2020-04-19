/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.RetencionimpuestodetDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuestodet;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestodetServicio extends AppGenericService<Retencionimpuestodet, String> {

	@Inject
	private RetencionimpuestodetDao retencionimpuestodetDao;
	

	@Override
	public List<Retencionimpuestodet> consultarActivos() {
		return null;
	}

	@Override
	public List<Retencionimpuestodet> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Retencionimpuestodet, String> getDao() {
		return retencionimpuestodetDao;
	}

}
