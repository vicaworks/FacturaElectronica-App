/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.TipocomprobanteDao;
import com.vcw.falecpv.core.modelo.persistencia.Tipocomprobante;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TipocomprobanteServicio extends AppGenericService<Tipocomprobante, String> {

	@Inject
	private TipocomprobanteDao tipocomprobanteDao;
	
	@Override
	public List<Tipocomprobante> consultarActivos() {
		return null;
	}

	@Override
	public List<Tipocomprobante> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Tipocomprobante, String> getDao() {
		return null;
	}

	/**
	 * @return the tipocomprobanteDao
	 */
	public TipocomprobanteDao getTipocomprobanteDao() {
		return tipocomprobanteDao;
	}

}
