/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Iva;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class IvaDao extends AppGenericDao<Iva, String> {

	/**
	 * @param type
	 */
	public IvaDao() {
		super(Iva.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Iva> getByEstado(EstadoRegistroEnum estadoRegistroEnum,String idEmpresa)throws DaoException{
		try {
			Query q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.empresa.idempresa=:idempresa ORDER BY i.porcentaje");
			q.setParameter("idempresa", idEmpresa);
			return q.getResultList();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public Iva getDefecto(String idEmpresa)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT i FROM Iva i WHERE i.empresa.idempresa=:idempresa AND i.defecto=1 ORDER BY i.porcentaje");
			q.setParameter("idempresa", idEmpresa);
			
			List<Iva> lista = q.getResultList();
			
			if(lista.size()>0) return lista.get(0);
			
			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
