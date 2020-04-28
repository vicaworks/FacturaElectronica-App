/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.DetalledestinatarioDao;
import com.vcw.falecpv.core.modelo.persistencia.Detalledestinatario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DetalledestinatarioServicio extends AppGenericService<Detalledestinatario, String> {

	@Inject
	private DetalledestinatarioDao detalledestinatarioDao;
	
	
	/**
	 * 
	 */
	public DetalledestinatarioServicio() {
	}

	@Override
	public List<Detalledestinatario> consultarActivos() {
		return null;
	}

	@Override
	public List<Detalledestinatario> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Detalledestinatario, String> getDao() {
		return detalledestinatarioDao;
	}

	/**
	 * @return the detalledestinatarioDao
	 */
	public DetalledestinatarioDao getDetalledestinatarioDao() {
		return detalledestinatarioDao;
	}

}
