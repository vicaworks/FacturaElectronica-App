/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.RetenciondetalleDao;
import com.vcw.falecpv.core.modelo.persistencia.Retenciondetalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetenciondetalleServicio extends AppGenericService<Retenciondetalle, String> {

	@Inject
	private RetenciondetalleDao retenciondetalleDao;
	
	@Override
	public List<Retenciondetalle> consultarActivos() {
		return null;
	}

	@Override
	public List<Retenciondetalle> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Retenciondetalle, String> getDao() {
		return retenciondetalleDao;
	}

	/**
	 * @return the retenciondetalleDao
	 */
	public RetenciondetalleDao getRetenciondetalleDao() {
		return retenciondetalleDao;
	}

}
