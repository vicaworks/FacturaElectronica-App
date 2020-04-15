/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.inject.Inject;

import javax.ejb.Stateless;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.BancoDao;
import com.vcw.falecpv.core.modelo.persistencia.Banco;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class BancoServicio extends AppGenericService<Banco, String> {

	@Inject
	private BancoDao bancoDao;
	
	
	@Override
	public List<Banco> consultarActivos() {
		return null;
	}

	@Override
	public List<Banco> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Banco, String> getDao() {
		return null;
	}

	/**
	 * @return the bancoDao
	 */
	public BancoDao getBancoDao() {
		return bancoDao;
	}

}
