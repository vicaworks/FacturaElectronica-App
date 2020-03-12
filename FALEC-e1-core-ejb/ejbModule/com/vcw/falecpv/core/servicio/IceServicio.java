/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.IceDao;
import com.vcw.falecpv.core.modelo.persistencia.Ice;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IceServicio extends AppGenericService<Ice, String> {

	@Inject
	private IceDao iceDao;
	
	/**
	 * 
	 */
	public IceServicio() {
	}

	@Override
	public List<Ice> consultarActivos() {
		return null;
	}

	@Override
	public List<Ice> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Ice, String> getDao() {
		return iceDao;
	}

}
