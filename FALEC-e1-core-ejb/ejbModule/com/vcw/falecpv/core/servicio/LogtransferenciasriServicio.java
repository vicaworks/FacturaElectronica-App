/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.LogtransferenciasriDao;
import com.vcw.falecpv.core.modelo.persistencia.Logtransferenciasri;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class LogtransferenciasriServicio extends AppGenericService<Logtransferenciasri, String> {
	
	@Inject
	private LogtransferenciasriDao logtransferenciasriDao;

	@Override
	public List<Logtransferenciasri> consultarActivos() {
		return null;
	}

	@Override
	public List<Logtransferenciasri> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Logtransferenciasri, String> getDao() {
		return logtransferenciasriDao;
	}

	/**
	 * @return the logtransferenciasriDao
	 */
	public LogtransferenciasriDao getLogtransferenciasriDao() {
		return logtransferenciasriDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public List<Logtransferenciasri> getByIdCabecera(String idCabecera)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(logtransferenciasriDao.getEntityManager());
			
			return qb.select("l")
					.from(Logtransferenciasri.class, "l")
					.equals("l.cabecera.idcabecera", idCabecera)
					.orderBy("l.fecha DESC").getResultList();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
