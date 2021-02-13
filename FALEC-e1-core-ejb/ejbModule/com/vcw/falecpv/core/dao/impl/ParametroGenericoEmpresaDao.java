/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.parametrosgenericos.ParametroGenericoBaseEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.ParametroGenericoEmpresa;

/**
 * @author cristianvillarreal
 *
 */
/**
 * @author isitk
 *
 */
/**
 * @author isitk
 *
 */
@Stateless
public class ParametroGenericoEmpresaDao extends AppGenericDao<ParametroGenericoEmpresa, String> {

	/**
	 * @param type
	 */
	public ParametroGenericoEmpresaDao() {
		super(ParametroGenericoEmpresa.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param parametroId
	 * @return
	 */
	public ParametroGenericoEmpresa getByEmpresa(String idEmpresa,ParametroGenericoBaseEnum parametroId) {
		Query q = getEntityManager().createQuery("SELECT p FROM ParametroGenericoEmpresa p WHERE p.idempresa=:idEmpresa AND p.idparametroempresa=:id");
		q.setParameter("idEmpresa", idEmpresa);
		q.setParameter("id", parametroId.getId());
		try {
			return (ParametroGenericoEmpresa) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param parametroId
	 * @return
	 */
	public ParametroGenericoEmpresa getByEstablecimiento(String idEstablecimiento,ParametroGenericoBaseEnum parametroId) {
		Query q = getEntityManager().createQuery("SELECT p FROM ParametroGenericoEmpresa p WHERE p.idestablecimiento=:idestablecimiento AND p.idparametroempresa=:id");
		q.setParameter("idestablecimiento", idEstablecimiento);
		q.setParameter("id", parametroId.getId());
		try {
			return (ParametroGenericoEmpresa) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
	/**
	 * @author isitkm
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<ParametroGenericoEmpresa>  getParamEmpresa(String idestablecimiento) throws DaoException {
		Query q = getEntityManager()
				.createQuery("SELECT p FROM ParametroGenericoEmpresa p WHERE p.idestablecimiento=:idestablecimiento ");
		q.setParameter("idestablecimiento", idestablecimiento);
		try {
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	/**
	 * @param idEmpresa
	 * @param idParametro
	 * @return
	 * @throws DaoException
	 */
	public boolean getByIdAndEmpresa(String idEmpresa,String idParametro)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT p FROM  ParametroGenericoEmpresa p WHERE p.idempresa=:idEmpresa and p.idparametroempresa=:idparametroempresa");
			q.setParameter("idEmpresa", idEmpresa);
			q.setParameter("idparametroempresa", idParametro);
			
			return (ParametroGenericoEmpresa) q.getSingleResult()!=null;
			
		} catch (NoResultException e) {
			return false;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEstablecimiento
	 * @param idParametro
	 * @return
	 * @throws DaoException
	 */
	public boolean getByIdAndEstablecimiento(String idEstablecimiento,String idParametro)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT p FROM  ParametroGenericoEmpresa p WHERE p.idestablecimiento=:idEstablecimiento and p.idparametroempresa=:idparametroempresa");
			q.setParameter("idEstablecimiento", idEstablecimiento);
			q.setParameter("idparametroempresa", idParametro);
			
			return (ParametroGenericoEmpresa) q.getSingleResult()!=null;
			
		} catch (NoResultException e) {
			return false;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
		
}
