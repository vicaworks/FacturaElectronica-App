/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.vcw.falecpv.core.dao.impl.TransaccionconceptoDao;
import com.vcw.falecpv.core.modelo.persistencia.Transaccionconcepto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransaccionconceptoServicio extends AppGenericService<Transaccionconcepto, String> {

	@Inject
	private TransaccionconceptoDao transaccionconceptoDao;
	
	/**
	 * 
	 */
	public TransaccionconceptoServicio() {
	}

	@Override
	public List<Transaccionconcepto> consultarActivos() {
		return null;
	}

	@Override
	public List<Transaccionconcepto> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Transaccionconcepto, String> getDao() {
		return transaccionconceptoDao;
	}

	/**
	 * @return the transaccionconceptoDao
	 */
	public TransaccionconceptoDao getTransaccionconceptoDao() {
		return transaccionconceptoDao;
	}

}
