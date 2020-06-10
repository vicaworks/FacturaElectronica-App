/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.DetalleimpuestoDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalleimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalleimpuestoServicio extends AppGenericService<Detalleimpuesto, String> {

	@Inject
	private DetalleimpuestoDao detalleimpuestoDao;
	
	
	/**
	 * 
	 */
	public DetalleimpuestoServicio() {
	}

	@Override
	public List<Detalleimpuesto> consultarActivos() {
		return null;
	}

	@Override
	public List<Detalleimpuesto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Detalleimpuesto, String> getDao() {
		return detalleimpuestoDao;
	}

	/**
	 * @return the detalleimpuestoDao
	 */
	public DetalleimpuestoDao getDetalleimpuestoDao() {
		return detalleimpuestoDao;
	}

}
