/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.TransacciontipoDao;
import com.vcw.falecpv.core.modelo.persistencia.Transacciontipo;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransacciontipoServicio extends AppGenericService<Transacciontipo, String> {

	@Inject
	private TransacciontipoDao transacciontipoDao;
	
	/**
	 * 
	 */
	public TransacciontipoServicio() {
	}

	@Override
	public List<Transacciontipo> consultarActivos() {
		return null;
	}

	@Override
	public List<Transacciontipo> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Transacciontipo, String> getDao() {
		return transacciontipoDao;
	}

	/**
	 * @return the transacciontipoDao
	 */
	public TransacciontipoDao getTransacciontipoDao() {
		return transacciontipoDao;
	}

}
