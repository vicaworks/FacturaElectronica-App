/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NonUniqueResultException;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.RetencionimpuestodetDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuestodet;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestodetServicio extends AppGenericService<Retencionimpuestodet, String> {

	@Inject
	private RetencionimpuestodetDao retencionimpuestodetDao;
	

	@Override
	public List<Retencionimpuestodet> consultarActivos() {
		return null;
	}

	@Override
	public List<Retencionimpuestodet> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Retencionimpuestodet, String> getDao() {
		return retencionimpuestodetDao;
	}

	/**
	 * @return the retencionimpuestodetDao
	 */
	public RetencionimpuestodetDao getRetencionimpuestodetDao() {
		return retencionimpuestodetDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param codigoImpuesto
	 * @param codigoDetalle
	 * @return
	 * @throws DaoException
	 */
	public Retencionimpuestodet getByCodSri(String codigoImpuesto,String codigoDetalle)throws DaoException{
		try {
				
			QueryBuilder qb = new QueryBuilder(retencionimpuestodetDao.getEntityManager());
			
			return (Retencionimpuestodet)qb.select("r")
					.from(Retencionimpuestodet.class,"r")
					.equals("r.retencionimpuesto.codigo",codigoImpuesto)
					.equals("r.codigo",codigoDetalle).getSingleResult();
			
		} catch (NonUniqueResultException e) {
			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
