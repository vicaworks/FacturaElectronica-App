/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.TotalimpuestoDao;
import com.vcw.falecpv.core.modelo.persistencia.Totalimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TotalimpuestoServicio extends AppGenericService<Totalimpuesto, String> {
	
	@Inject
	private TotalimpuestoDao totalimpuestoDao;

	/**
	 * 
	 */
	public TotalimpuestoServicio() {
	}

	@Override
	public List<Totalimpuesto> consultarActivos() {
		return null;
	}

	@Override
	public List<Totalimpuesto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Totalimpuesto, String> getDao() {
		return totalimpuestoDao;
	}

	/**
	 * @return the totalimpuestoDao
	 */
	public TotalimpuestoDao getTotalimpuestoDao() {
		return totalimpuestoDao;
	}

}
