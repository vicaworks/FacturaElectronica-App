/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.DestinatarioDao;
import com.vcw.falecpv.core.modelo.persistencia.Destinatario;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DestinatarioServicio extends AppGenericService<Destinatario, String> {

	@Inject
	private DestinatarioDao destinatarioDao;
	
	
	/**
	 * 
	 */
	public DestinatarioServicio() {
	}

	@Override
	public List<Destinatario> consultarActivos() {
		return null;
	}

	@Override
	public List<Destinatario> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Destinatario, String> getDao() {
		return destinatarioDao;
	}

	/**
	 * @return the destinatarioDao
	 */
	public DestinatarioDao getDestinatarioDao() {
		return destinatarioDao;
	}

}
