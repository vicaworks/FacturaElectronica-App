/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCTransaccion;
import com.vcw.falecpv.core.dao.impl.TransaccionDao;
import com.vcw.falecpv.core.modelo.persistencia.Transaccion;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TransaccionServicio extends AppGenericService<Transaccion, String> {

	@Inject
	private TransaccionDao transaccionDao;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	/**
	 * 
	 */
	public TransaccionServicio() {
	}

	@Override
	public List<Transaccion> consultarActivos() {
		return null;
	}

	@Override
	public List<Transaccion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Transaccion, String> getDao() {
		return transaccionDao;
	}

	/**
	 * @return the transaccionDao
	 */
	public TransaccionDao getTransaccionDao() {
		return transaccionDao;
	}

	/**
	 * @author cristianvillarreal
	 *
	 */
	public Transaccion guardarFacade(Transaccion t) throws DaoException {
		
		try {
			
			if (t.getIdtransaccion()==null) {
				t.setIdtransaccion(contadorPkServicio.generarContadorTabla(TCTransaccion.TRANSACCION, t.getEstablecimiento().getIdestablecimiento()));
				super.crear(t);
			}else {
				super.actualizar(t);
			}
			
			return t;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	
}
