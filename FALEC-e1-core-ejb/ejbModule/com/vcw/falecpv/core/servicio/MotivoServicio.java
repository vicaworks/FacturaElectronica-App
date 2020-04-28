/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.MotivoDao;
import com.vcw.falecpv.core.modelo.persistencia.Motivo;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class MotivoServicio extends AppGenericService<Motivo, String> {
	
	@Inject
	private MotivoDao motivoDao;
	
	
	/**
	 * 
	 */
	public MotivoServicio() {
	}

	@Override
	public List<Motivo> consultarActivos() {
		return null;
	}

	@Override
	public List<Motivo> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Motivo, String> getDao() {
		return motivoDao;
	}

	/**
	 * @return the motivoDao
	 */
	public MotivoDao getMotivoDao() {
		return motivoDao;
	}

}
