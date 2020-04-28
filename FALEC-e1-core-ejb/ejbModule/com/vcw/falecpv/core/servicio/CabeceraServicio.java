package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.CabeceraDao;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;

@Stateless
public class CabeceraServicio extends AppGenericService<Cabecera, String> {

	@Inject
	private CabeceraDao cabeceraDao;
	
	
	public CabeceraServicio() {
	}

	@Override
	public List<Cabecera> consultarActivos() {
		return null;
	}

	@Override
	public List<Cabecera> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Cabecera, String> getDao() {
		return cabeceraDao;
	}

	/**
	 * @return the cabeceraDao
	 */
	public CabeceraDao getCabeceraDao() {
		return cabeceraDao;
	}

}
