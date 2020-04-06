package com.vcw.falecpv.core.servicio;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.TipoIdentificacionDao;
import com.vcw.falecpv.core.modelo.persistencia.TipoIdentificacion;

/**
 * @author Jorge Toaza
 *
 */
@Stateless
public class TipoIdentificacionServicio extends AppGenericService<TipoIdentificacion, String> {

	@Inject
	private TipoIdentificacionDao tipoIdentificacionDao;
	
	/**
	 * 
	 */
	public TipoIdentificacionServicio() {
	}

	@Override
	public List<TipoIdentificacion> consultarActivos() {
		return null;
	}

	@Override
	public List<TipoIdentificacion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<TipoIdentificacion, String> getDao() {
		return tipoIdentificacionDao;
	}

	/**
	 * @return the tipoIdentificacionDao
	 */
	public TipoIdentificacionDao getTipoIdentificacionDao() {
		return tipoIdentificacionDao;
	}
}