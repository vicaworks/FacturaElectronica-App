/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.ImpuestoretencionDao;
import com.vcw.falecpv.core.modelo.persistencia.Impuestoretencion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ImpuestoretencionServicio extends AppGenericService<Impuestoretencion, String> {

	@Inject
	private ImpuestoretencionDao impuestoretencionDao;
	
	
	/**
	 * 
	 */
	public ImpuestoretencionServicio() {
	}

	@Override
	public List<Impuestoretencion> consultarActivos() {
		return null;
	}

	@Override
	public List<Impuestoretencion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Impuestoretencion, String> getDao() {
		return impuestoretencionDao;
	}

	/**
	 * @return the impuestoretencionDao
	 */
	public ImpuestoretencionDao getImpuestoretencionDao() {
		return impuestoretencionDao;
	}

}
