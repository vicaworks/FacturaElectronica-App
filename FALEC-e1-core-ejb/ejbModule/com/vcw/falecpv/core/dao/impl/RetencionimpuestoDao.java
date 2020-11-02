/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Retencionimpuesto;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class RetencionimpuestoDao extends AppGenericDao<Retencionimpuesto, String> {

	public RetencionimpuestoDao() {
		super(Retencionimpuesto.class);
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estadoRegistroEnum
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Retencionimpuesto> getByEstado(EstadoRegistroEnum estadoRegistroEnum)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT r FROM Retencionimpuesto r WHERE r.estado=:estado ");
			q.setParameter("estado", estadoRegistroEnum.getInicial());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param codigoSri
	 * @return
	 * @throws DaoException
	 */
	public Retencionimpuesto getByCodigoSri(String codigoSri)throws DaoException{
		try {
			
			Query q = getEntityManager().createQuery("SELECT r FROM Retencionimpuesto r WHERE r.codigo=:codigo ");
			q.setParameter("codigo", codigoSri);
			
			return (Retencionimpuesto) q.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}  catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
