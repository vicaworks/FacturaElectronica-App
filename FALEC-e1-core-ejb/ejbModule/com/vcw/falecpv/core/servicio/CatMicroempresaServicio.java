/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.CatMicroempresaDao;
import com.vcw.falecpv.core.modelo.persistencia.CatMicroempresa;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class CatMicroempresaServicio extends AppGenericService<CatMicroempresa, String> {
	
	@Inject
	private CatMicroempresaDao catMicroempresaDao;
	
	/**
	 * 
	 */
	public CatMicroempresaServicio() {
	}

	@Override
	public List<CatMicroempresa> consultarActivos() {
		return null;
	}

	@Override
	public List<CatMicroempresa> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<CatMicroempresa, String> getDao() {
		return catMicroempresaDao;
	}

	/**
	 * @return the catMicroempresaDao
	 */
	public CatMicroempresaDao getCatMicroempresaDao() {
		return catMicroempresaDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param ruc
	 * @param anio
	 * @return
	 * @throws DaoException
	 */
	public boolean esMicroEmpresa(String ruc,Integer anio)throws DaoException{
		try {
			
			QueryBuilder q = new QueryBuilder(catMicroempresaDao.getEntityManager());
			
			return q.select("p")
				.from(CatMicroempresa.class,"p")	
				.equals("p.ruc",ruc)
				.equals("p.anio", anio).getResultList().size()>0;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
