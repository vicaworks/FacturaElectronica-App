/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.ComprobanterecibidoDao;
import com.vcw.falecpv.core.modelo.persistencia.Comprobanterecibido;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class ComprobanterecibidoServicio extends AppGenericService<Comprobanterecibido, String> {
	
	@Inject
	private ComprobanterecibidoDao  comprobanterecibidoDao;

	@Override
	public List<Comprobanterecibido> consultarActivos() {
		return null;
	}

	@Override
	public List<Comprobanterecibido> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Comprobanterecibido, String> getDao() {
		return comprobanterecibidoDao;
	}

	/**
	 * @return the comprobanterecibidoDao
	 */
	public ComprobanterecibidoDao getComprobanterecibidoDao() {
		return comprobanterecibidoDao;
	}


}
