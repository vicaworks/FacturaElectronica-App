/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.InfoadicionalDao;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class InfoadicionalServicio extends AppGenericService<Infoadicional, String> {

	@Inject
	private InfoadicionalDao infoadicionalDao;
	
	/**
	 * 
	 */
	public InfoadicionalServicio() {
	}

	@Override
	public List<Infoadicional> consultarActivos() {
		return null;
	}

	@Override
	public List<Infoadicional> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Infoadicional, String> getDao() {
		return infoadicionalDao;
	}

	/**
	 * @return the infoadicionalDao
	 */
	public InfoadicionalDao getInfoadicionalDao() {
		return infoadicionalDao;
	}

}
