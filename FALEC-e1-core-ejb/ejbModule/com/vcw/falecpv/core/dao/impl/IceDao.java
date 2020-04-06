/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Ice;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IceDao extends AppGenericDao<Ice, String> {

	
	/**
	 * @param type
	 */
	public IceDao() {
		super(Ice.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Ice> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEmpresa)throws DaoException{
		try {
			Query q = getEntityManager().createQuery("SELECT i FROM Ice i WHERE i.empresa.idempresa=:idempresa ORDER BY i.descripcion");
			q.setParameter("idempresa", idEmpresa);
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	

	/**
	 * @author Isabel Lobato
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Ice> getAllIce(String idEmpresa)throws DaoException{
		try {
			List<Ice> lista = new ArrayList<>();
			Query q = getEntityManager()
					.createQuery("SELECT i FROM Ice i WHERE i.empresa.idempresa=:idempresa ORDER BY i.codigo");
			q.setParameter("idempresa", idEmpresa);

			lista = q.getResultList();

			return lista;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author Isabel Lobato
	 * @param codigo
	 * @param idice
	 * @return
	 * @throws DaoException
	 */
	public boolean existeCodigo(String codigo, String idice)throws DaoException{
		try {
			
			Query q = null;
			
			if(idice!=null) {
				q = getEntityManager().createQuery("SELECT i FROM Ice i  WHERE i.codigo=:codigo AND i.idice<>:idice");
				q.setParameter("codigo", codigo);
				q.setParameter("idice", idice);
				
			}else {
				q = getEntityManager().createQuery("SELECT i FROM Ice i  WHERE i.codigo=:codigo ");
				q.setParameter("codigo", codigo);
			}
		
			if(q.getResultList().size()>0) {
				return true;
				}
			
			return false;
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
