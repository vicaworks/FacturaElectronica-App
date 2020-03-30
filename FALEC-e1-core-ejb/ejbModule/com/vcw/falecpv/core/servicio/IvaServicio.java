/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.IvaDao;
import com.vcw.falecpv.core.modelo.persistencia.Iva;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IvaServicio extends AppGenericService<Iva, String> {

	@Inject
	private IvaDao ivaDao;
	
	/**
	 * 
	 */
	public IvaServicio() {
	}

	@Override
	public List<Iva> consultarActivos() {
		return null;
	}

	@Override
	public List<Iva> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Iva, String> getDao() {
		return ivaDao;
	}

	/**
	 * @return the ivaDao
	 */
	public IvaDao getIvaDao() {
		return ivaDao;
	}


}
