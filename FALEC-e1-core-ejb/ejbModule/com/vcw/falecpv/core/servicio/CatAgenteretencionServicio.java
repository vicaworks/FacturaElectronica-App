/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.CatAgenteretencionDao;
import com.vcw.falecpv.core.modelo.persistencia.CatAgenteretencion;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CatAgenteretencionServicio extends AppGenericService<CatAgenteretencion, String> {

	@Inject
	private CatAgenteretencionDao catAgenteretencionDao;
	
	/**
	 * 
	 */
	public CatAgenteretencionServicio() {
	}

	@Override
	public List<CatAgenteretencion> consultarActivos() {
		return null;
	}

	@Override
	public List<CatAgenteretencion> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<CatAgenteretencion, String> getDao() {
		return catAgenteretencionDao;
	}

	/**
	 * @return the catAgenteretencionDao
	 */
	public CatAgenteretencionDao getCatAgenteretencionDao() {
		return catAgenteretencionDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param ruc
	 * @return
	 * @throws DaoException
	 */
	public boolean esAgenteRetencion(String ruc)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(catAgenteretencionDao.getEntityManager());
			
			return q.select("p")
				.from(CatAgenteretencion.class,"p")	
				.equals("p.ruc",ruc).getResultList().size()>0;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
