/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.SegperfilpredefinidoDao;
import com.vcw.falecpv.core.dao.impl.SegperfilpredefinidoperfilDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilpredefinido;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilpredefinidoServicio extends AppGenericService<Segperfilpredefinido, String> {

	@Inject
	private SegperfilpredefinidoDao segperfilpredefinidoDao;
	
	@Inject
	private SegperfilpredefinidoperfilDao segperfilpredefinidoperfilDao;
	
	/**
	 * 
	 */
	public SegperfilpredefinidoServicio() {
	}

	@Override
	public List<Segperfilpredefinido> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfilpredefinido> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfilpredefinido, String> getDao() {
		return segperfilpredefinidoDao;
	}

	/**
	 * @return the segperfilpredefinidoDao
	 */
	public SegperfilpredefinidoDao getSegperfilpredefinidoDao() {
		return segperfilpredefinidoDao;
	}

	/**
	 * @return the segperfilpredefinidoperfilDao
	 */
	public SegperfilpredefinidoperfilDao getSegperfilpredefinidoperfilDao() {
		return segperfilpredefinidoperfilDao;
	}

}
