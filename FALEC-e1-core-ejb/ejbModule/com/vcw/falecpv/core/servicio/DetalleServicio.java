/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.DetalleDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalleServicio extends AppGenericService<Detalle, String> {

	@Inject
	private DetalleDao detalleDao;
	
	/**
	 * 
	 */
	public DetalleServicio() {
	}

	@Override
	public List<Detalle> consultarActivos() {
		return null;
	}

	@Override
	public List<Detalle> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Detalle, String> getDao() {
		return detalleDao;
	}

	/**
	 * @return the detalleDao
	 */
	public DetalleDao getDetalleDao() {
		return detalleDao;
	}

}
